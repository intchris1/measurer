package com.offsidegaming.measurer.web;

import com.offsidegaming.measurer.AbstractIntegrationTest;
import com.offsidegaming.measurer.entity.Measurement;
import com.offsidegaming.measurer.service.dto.MeasurementDTO;
import com.offsidegaming.measurer.service.dto.SearchMeasurementCriteria;
import com.offsidegaming.measurer.service.dto.filter.InstantFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static com.offsidegaming.measurer.TestObjects.getMeasurement;
import static com.offsidegaming.measurer.TestObjects.getValidCreateMeasurementRq;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MeasurementResourceTest extends AbstractIntegrationTest {

    @Test
    void givenAuthenticatedUser_createMeasurement_ok() throws Exception {
        String user = "user1";
        MeasurementDTO rq = getValidCreateMeasurementRq();

        String response = mockMvc.perform(post("/api/measurements")
                        .with(httpBasic(user, "user1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(rq)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        MeasurementDTO measurementDTO = objectMapper.readValue(response, MeasurementDTO.class);
        Measurement measurement = measurementRepository.findById(measurementDTO.getId()).orElseThrow();
        var partialComparator = new CustomComparator(JSONCompareMode.STRICT,
                new Customization("lastModifiedDate", (v1, v2) -> true),
                new Customization("id", (v1, v2) -> true),
                new Customization("createdDate", (v1, v2) -> true));
        assertAll(
                () -> assertThat(measurement.getCreatedBy()).isEqualTo(user),
                () -> assertThat(measurement.getLastModifiedBy()).isEqualTo(user),
                () -> assertThat(measurement.getLastModifiedDate()).isNotNull(),
                () -> assertThat(measurement.getCreatedDate()).isNotNull(),
                () -> assertThat(measurement.getMeasurement()).isEqualTo(rq.getMeasurement()),
                () -> assertThat(measurement.getUsername()).isEqualTo(user),
                () -> JSONAssert.assertEquals(readResource("/json/createMeasurementResponseDTO.json"), response, partialComparator)
        );
    }

    @Test
    void givenAuthenticatedAdmin_createMeasurement_ok() throws Exception {
        String admin = "admin";
        MeasurementDTO rq = getValidCreateMeasurementRq();
        rq.setUsername("user1");

        String response = mockMvc.perform(post("/api/measurements")
                        .with(httpBasic(admin, admin))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(rq)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        MeasurementDTO measurementDTO = objectMapper.readValue(response, MeasurementDTO.class);
        Measurement measurement = measurementRepository.findById(measurementDTO.getId()).orElseThrow();
        assertAll(
                () -> assertThat(measurement.getCreatedBy()).isEqualTo(admin),
                () -> assertThat(measurement.getLastModifiedBy()).isEqualTo(admin),
                () -> assertThat(measurement.getUsername()).isEqualTo("user1")
        );
    }

    @ParameterizedTest
    @MethodSource("validationErrorExamples")
    void givenAuthenticatedUser_createMeasurement_validationError(String content, String response) throws Exception {
        String user = "user1";

        String problem = mockMvc.perform(post("/api/measurements")
                        .with(httpBasic(user, "user1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals(response, problem, true);
    }

    @Test
    void givenUnauthenticatedUser_createMeasurement_notAuthorized() throws Exception {
        mockMvc.perform(post("/api/measurements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void givenUserWithWrongRole_createMeasurement_forbidden() throws Exception {
        MeasurementDTO rq = getValidCreateMeasurementRq();

        mockMvc.perform(post("/api/measurements")
                        .with(httpBasic("moderator", "moderator"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(rq)))
                .andExpect(status().isForbidden());
    }

    @Test
    void givenAuthenticatedAdmin_searchMeasurementsWithoutCriteria_ok() throws Exception {
        Measurement m1 = getMeasurement("1");
        measurementRepository.save(m1);
        Measurement m2 = getMeasurement("2");
        measurementRepository.save(m2);
        Measurement m3 = getMeasurement("3");
        measurementRepository.save(m3);

        mockMvc.perform(put("/api/measurements/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                        .with(httpBasic("admin", "admin")))
                .andExpectAll(status().isOk(),
                        header().string("X-Total-Count", "3"),
                        jsonPath("$").value(hasSize(3)),
                        jsonPath("$.[*].username").value(hasItem(m1.getUsername())),
                        jsonPath("$.[*].username").value(hasItem(m2.getUsername())),
                        jsonPath("$.[*].username").value(hasItem(m3.getUsername()))
                );
    }

    @Test
    void givenAuthenticatedAdmin_searchMeasurementsByUsername_ok() throws Exception {
        Measurement m1 = getMeasurement("1");
        measurementRepository.save(m1);
        Measurement m2 = getMeasurement("2");
        measurementRepository.save(m2);
        Measurement m3 = getMeasurement("3");
        measurementRepository.save(m3);

        SearchMeasurementCriteria findByUsernameRq = new SearchMeasurementCriteria().setUsername(m1.getUsername());
        mockMvc.perform(put("/api/measurements/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(findByUsernameRq))
                        .with(httpBasic("admin", "admin")))
                .andExpectAll(status().isOk(),
                        jsonPath("$").value(hasSize(1)),
                        jsonPath("$.[*].username").value(hasItem(m1.getUsername()))
                );
    }

    @Test
    void givenAuthenticatedAdmin_searchMeasurementsByCreatedDate_ok() throws Exception {
        Measurement m1 = getMeasurement("1");
        measurementRepository.save(m1);
        Measurement m2 = getMeasurement("2");
        measurementRepository.save(m2);
        Measurement m3 = getMeasurement("3");
        measurementRepository.save(m3);

        SearchMeasurementCriteria findByCreatedDateRq = new SearchMeasurementCriteria().setCreatedDate(
                new InstantFilter().setLessThan(m3.getCreatedDate())
        );
        mockMvc.perform(put("/api/measurements/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(findByCreatedDateRq))
                        .with(httpBasic("admin", "admin")))
                .andExpectAll(status().isOk(),
                        jsonPath("$").value(hasSize(2)),
                        jsonPath("$.[*].username").value(hasItems(m1.getUsername(), m2.getUsername()))
                );
    }

    private static Stream<Arguments> validationErrorExamples() {
        return Stream.of(
                Arguments.of("{}", readResource("json/createMeasurementBadRequest1.json")),
                Arguments.of(readResource("json/invalidCreateMeasurementRq2.json"),
                        readResource("json/createMeasurementBadRequest2.json")),
                Arguments.of(readResource("json/invalidCreateMeasurementRq3.json"),
                        readResource("json/createMeasurementBadRequest3.json"))
        );
    }
}