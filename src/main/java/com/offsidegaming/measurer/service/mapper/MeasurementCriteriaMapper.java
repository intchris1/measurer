package com.offsidegaming.measurer.service.mapper;

import com.offsidegaming.measurer.service.dto.MeasurementDTO;
import com.offsidegaming.measurer.service.dto.SearchMeasurementCriteria;
import com.querydsl.core.types.Predicate;

public interface MeasurementCriteriaMapper {

    Predicate toPredicate(SearchMeasurementCriteria criteria);
}
