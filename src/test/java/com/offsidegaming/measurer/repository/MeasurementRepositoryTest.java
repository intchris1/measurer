package com.offsidegaming.measurer.repository;

import com.offsidegaming.measurer.AbstractIntegrationTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static com.offsidegaming.measurer.TestObjects.getMeasurement;
import static org.assertj.core.api.Assertions.assertThat;

class MeasurementRepositoryTest extends AbstractIntegrationTest {

    @ParameterizedTest
    @CsvSource({
            "2022-10-10,2022-11-11,conflict",
            "2022-10-09,2022-11-11,conflict",
            "2022-10-09,2022-11-10,conflict",
            "2022-11-11,2022-11-11,conflict",
            "2022-11-12,2022-11-12,ok",
            "2022-09-10,2022-10-09,ok",
            "2022-12-11,2022-12-12,ok",
            "2022-11-10,2022-11-11,conflict",
            "2022-11-10,2022-11-12,conflict"
    })
    void findAllWithConflictDatesForGivenUsernameAndMeasurements(String startDate, String endDate, String result) {
        var measurement = getMeasurement(
                LocalDate.of(2022, 10, 10),
                LocalDate.of(2022, 11, 11)
        );
        measurementRepository.save(measurement);

        var ids = measurementRepository.findAllWithConflictDatesForGivenUsernameAndMeasurements(
                LocalDate.parse(startDate), LocalDate.parse(endDate), measurement.getUsername(),
                measurement.getMeasurementType());

        assertThat(ids.isEmpty()).isEqualTo(!result.equals("conflict"));
    }
}