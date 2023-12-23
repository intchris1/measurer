package com.offsidegaming.measurer;

import com.offsidegaming.measurer.entity.Measurement;
import com.offsidegaming.measurer.entity.MeasurementType;
import com.offsidegaming.measurer.service.dto.MeasurementDTO;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDate;

@UtilityClass
public class TestObjects {

    public static MeasurementDTO getValidCreateMeasurementRq() {
        return new MeasurementDTO()
                .setMeasurement(new BigDecimal("100"))
                .setMeasurementType(MeasurementType.GAS)
                .setStartDate(LocalDate.of(2023, 11, 22))
                .setEndDate(LocalDate.of(2023, 12, 22));
    }

    public static Measurement getMeasurement(String username) {
        return new Measurement()
                .setMeasurement(new BigDecimal("100"))
                .setMeasurementType(MeasurementType.GAS)
                .setStartDate(LocalDate.of(2023, 11, 22))
                .setEndDate(LocalDate.of(2023, 12, 22))
                .setUsername(username);
    }

    public static Measurement getMeasurement(LocalDate startDate, LocalDate endDate) {
        var measurement = getMeasurement("username");
        measurement.setStartDate(startDate);
        measurement.setEndDate(endDate);
        return measurement;
    }
}
