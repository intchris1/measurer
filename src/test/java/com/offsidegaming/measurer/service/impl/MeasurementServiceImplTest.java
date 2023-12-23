package com.offsidegaming.measurer.service.impl;

import com.offsidegaming.measurer.repository.MeasurementRepository;
import com.offsidegaming.measurer.service.dto.MeasurementDTO;
import com.offsidegaming.measurer.service.dto.SearchMeasurementCriteria;
import com.offsidegaming.measurer.service.mapper.MeasurementCriteriaMapper;
import com.offsidegaming.measurer.service.mapper.MeasurementMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import static com.offsidegaming.measurer.service.impl.MeasurementServiceImpl.USERNAME_NOT_MATCHED_ERROR;
import static com.offsidegaming.measurer.service.impl.MeasurementServiceImpl.USERNAME_NOT_SET_ERROR;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
class MeasurementServiceImplTest {

    @Mock
    private MeasurementRepository measurementRepository;

    @Mock
    private MeasurementCriteriaMapper measurementCriteriaMapper;

    @Mock
    private MeasurementMapper measurementMapper;

    @InjectMocks
    private MeasurementServiceImpl measurementService;

    @Test
    void givenEmptyUsername_createMeasurement_badRequest() {
        var rq = new MeasurementDTO();

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> measurementService.createMeasurement(rq))
                .matches(it -> it.getStatusCode().equals(HttpStatus.BAD_REQUEST))
                .withMessageContaining(USERNAME_NOT_SET_ERROR);
    }

    @Test
    @WithMockUser(roles = "ADMIN", username = "admin")
    void givenAdminAndBlankUsername_createMeasurement_usernameSetFromContext() {
        var rq = new MeasurementDTO();

        measurementService.createMeasurement(rq);

        assertAll(
                () -> verify(measurementMapper).toMeasurementEntity(argThat(it -> it.getUsername().equals("admin"))),
                () -> verify(measurementRepository).save(any())
        );
    }

    @Test
    @WithMockUser(roles = "USER", username = "user1")
    void givenUserAndBlankUsername_createMeasurement_usernameSetFromContext() {
        var rq = new MeasurementDTO();

        measurementService.createMeasurement(rq);

        assertAll(
                () -> verify(measurementMapper).toMeasurementEntity(argThat(it -> it.getUsername().equals("user1"))),
                () -> verify(measurementRepository).save(any())
        );
    }

    @Test
    @WithMockUser(roles = "USER", username = "user1")
    void givenUserAndDifferentUsername_createMeasurement_forbidden() {
        var rq = new MeasurementDTO().setUsername("different");

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> measurementService.createMeasurement(rq))
                .matches(it -> it.getStatusCode().equals(HttpStatus.FORBIDDEN))
                .withMessageContaining(USERNAME_NOT_MATCHED_ERROR.formatted(rq.getUsername(), "user1"));
    }

    @Test
    @WithMockUser(roles = "OTHER_ROLE", username = "user1")
    void givenUserWithDifferentRole_createMeasurement_badRequest() {
        var rq = new MeasurementDTO();

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> measurementService.createMeasurement(rq))
                .matches(it -> it.getStatusCode().equals(HttpStatus.BAD_REQUEST))
                .withMessageContaining(USERNAME_NOT_SET_ERROR);
    }

    @Test
    @WithMockUser(roles = "USER", username = "user1")
    void givenUserAndDifferentUsername_searchMeasurements_forbidden() {
        var rq = new SearchMeasurementCriteria().setUsername("different");

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> measurementService.searchMeasurements(null, rq))
                .matches(it -> it.getStatusCode().equals(HttpStatus.FORBIDDEN))
                .withMessageContaining(USERNAME_NOT_MATCHED_ERROR.formatted(rq.getUsername(), "user1"));
    }

    @Test
    @WithMockUser(roles = "USER", username = "user1")
    void givenUserAndBlankUsername_searchMeasurements_usernameSetFromContext() {
        var rq = new SearchMeasurementCriteria();

        measurementService.searchMeasurements(null, rq);

        verify(measurementCriteriaMapper).toPredicate(argThat(it -> it.getUsername().equals("user1")));
    }
}