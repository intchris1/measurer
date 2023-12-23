package com.offsidegaming.measurer.service.mapper;

import com.offsidegaming.measurer.entity.QMeasurement;
import com.offsidegaming.measurer.service.dto.SearchMeasurementCriteria;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import static com.offsidegaming.measurer.util.PredicateUtils.append;
import static com.offsidegaming.measurer.util.PredicateUtils.buildEnumPredicate;
import static com.offsidegaming.measurer.util.PredicateUtils.buildInstantPredicate;
import static com.offsidegaming.measurer.util.PredicateUtils.buildLocalDatePredicate;

@Component
public class MeasurementCriteriaMapperImpl implements MeasurementCriteriaMapper {

    public static final QMeasurement MEASUREMENT = QMeasurement.measurement1;

    @Override
    public Predicate toPredicate(SearchMeasurementCriteria criteria) {
        if (criteria == null) {
            return null;
        }
        BooleanExpression resultPredicate = null;
        if (criteria.getId() != null) {
            BooleanExpression idCriteria = MEASUREMENT.id.eq(criteria.getId());
            resultPredicate = append(resultPredicate, idCriteria);
        }
        if (criteria.getUsername() != null) {
            BooleanExpression usernameCriteria = MEASUREMENT.username.eq(criteria.getUsername());
            resultPredicate = append(resultPredicate, usernameCriteria);
        }
        if (criteria.getMeasurementType() != null) {
            resultPredicate = append(resultPredicate, buildEnumPredicate(criteria.getMeasurementType(), MEASUREMENT.measurementType));
        }
        if (criteria.getCreatedDate() != null) {
            resultPredicate = append(resultPredicate, buildInstantPredicate(criteria.getCreatedDate(), MEASUREMENT.createdDate));
        }
        if (criteria.getEndDate() != null) {
            resultPredicate = append(resultPredicate, buildLocalDatePredicate(criteria.getEndDate(), MEASUREMENT.endDate));
        }
        if (criteria.getStartDate() != null) {
            resultPredicate = append(resultPredicate, buildLocalDatePredicate(criteria.getStartDate(), MEASUREMENT.endDate));
        }
        return resultPredicate;
    }
}
