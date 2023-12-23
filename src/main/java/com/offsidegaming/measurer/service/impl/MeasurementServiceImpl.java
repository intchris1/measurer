package com.offsidegaming.measurer.service.impl;

import com.offsidegaming.measurer.entity.Measurement;
import com.offsidegaming.measurer.repository.MeasurementRepository;
import com.offsidegaming.measurer.service.MeasurementService;
import com.offsidegaming.measurer.service.dto.MeasurementDTO;
import com.offsidegaming.measurer.service.dto.SearchMeasurementCriteria;
import com.offsidegaming.measurer.service.mapper.MeasurementCriteriaMapper;
import com.offsidegaming.measurer.service.mapper.MeasurementMapper;
import com.offsidegaming.measurer.util.SecurityUtils;
import com.offsidegaming.measurer.util.SecurityUtils.UserData;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.offsidegaming.measurer.util.SecurityUtils.ADMIN_ROLE;
import static com.offsidegaming.measurer.util.SecurityUtils.USER_ROLE;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@RequiredArgsConstructor
@Slf4j
public class MeasurementServiceImpl implements MeasurementService {

    public static final String USERNAME_NOT_PRESENT_ERROR = "Username is not present in current authentication";
    public static final String USERNAME_NOT_MATCHED_ERROR = "Given username (%s) doesn't match authentication username (%s)";

    private final MeasurementRepository repository;
    private final MeasurementCriteriaMapper measurementCriteriaMapper;
    private final MeasurementMapper measurementMapper;

    @Override
    @Transactional
    public MeasurementDTO createMeasurement(MeasurementDTO measurementDTO) {
        setUserNameForCreation(measurementDTO);
        validateMeasurementDates(measurementDTO);
        Measurement entity = measurementMapper.toEntity(measurementDTO);
        Measurement saved = repository.save(entity);
        return measurementMapper.toMeasurementDto(saved);
    }

    @Override
    @Transactional
    public Page<MeasurementDTO> searchMeasurements(Pageable pageable, SearchMeasurementCriteria criteria) {
        setUserNameForSearch(criteria);
        Predicate predicate = measurementCriteriaMapper.toPredicate(criteria);
        Page<Measurement> result = predicate == null ? repository.findAll(pageable) : repository.findAll(predicate, pageable);
        return measurementMapper.toMeasurementDtoPage(result);
    }

    private static void setUserNameForCreation(MeasurementDTO measurementDTO) {
        UserData userData = SecurityUtils.getCurrentUserData();
        if (userData.getUsername() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, USERNAME_NOT_PRESENT_ERROR);
        }
        if (userData.getRoles().contains(ADMIN_ROLE) && isBlank(measurementDTO.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username must not be empty");
        }
        if (userData.getRoles().contains(USER_ROLE)) {
            if (isBlank(measurementDTO.getUsername())) {
                measurementDTO.setUsername(userData.getUsername());
                log.debug("Set username for create measurement request: {}", measurementDTO.getUsername());
            }
            if (!measurementDTO.getUsername().equals(userData.getUsername())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, USERNAME_NOT_MATCHED_ERROR
                        .formatted(measurementDTO.getUsername(), userData.getUsername()));
            }
        }
        if (isBlank(measurementDTO.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Couldn't set username for measurement creation");
        }
    }

    private static void setUserNameForSearch(SearchMeasurementCriteria searchMeasurementCriteria) {
        UserData userData = SecurityUtils.getCurrentUserData();
        if (userData.getUsername() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, USERNAME_NOT_PRESENT_ERROR);
        }
        if (!userData.getRoles().contains(ADMIN_ROLE) && userData.getRoles().contains(USER_ROLE)) {
            if (isBlank(searchMeasurementCriteria.getUsername())) {
                searchMeasurementCriteria.setUsername(userData.getUsername());
                log.debug("Set username for search request: {}", searchMeasurementCriteria.getUsername());
            }
            if (!searchMeasurementCriteria.getUsername().equals(userData.getUsername())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, USERNAME_NOT_MATCHED_ERROR
                        .formatted(searchMeasurementCriteria.getUsername(), userData.getUsername()));
            }
        }
    }

    private void validateMeasurementDates(MeasurementDTO measurementDTO) {
        List<Long> alreadyExistingMeasurements = repository.findAllByEndDateGreaterThanAndUsernameEqualsAndMeasurementEquals(
                measurementDTO.getStartDate(), measurementDTO.getUsername(), measurementDTO.getMeasurementType());
        if (!alreadyExistingMeasurements.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Given startDate, endDate are in conflict with previously created measurements for this user (identifiers: %s)".formatted(alreadyExistingMeasurements));
        }
    }
}
