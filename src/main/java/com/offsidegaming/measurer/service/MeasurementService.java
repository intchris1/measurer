package com.offsidegaming.measurer.service;

import com.offsidegaming.measurer.service.dto.SearchMeasurementCriteria;
import com.offsidegaming.measurer.service.dto.MeasurementDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MeasurementService {

    MeasurementDTO createMeasurement(MeasurementDTO measurementDTO);

    Page<MeasurementDTO> searchMeasurements(Pageable pageable, SearchMeasurementCriteria criteria);
}
