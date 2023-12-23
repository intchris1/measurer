package com.offsidegaming.measurer.util;

import com.offsidegaming.measurer.service.dto.filter.Filter;
import com.offsidegaming.measurer.service.dto.filter.InstantFilter;
import com.offsidegaming.measurer.service.dto.filter.LocalDateFilter;
import com.offsidegaming.measurer.service.dto.filter.RangeFilter;
import com.offsidegaming.measurer.service.dto.filter.StringFilter;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.SimpleExpression;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;

@UtilityClass
public class PredicateUtils {

    public static BooleanExpression buildLocalDatePredicate(LocalDateFilter filter, DatePath<LocalDate> path) {
        return toComparablePath(filter, path);
    }

    public static <T extends Enum<T>> BooleanExpression buildEnumPredicate(StringFilter filter, EnumPath<T> path) {
        @SuppressWarnings("unchecked") Class<T> type = (Class<T>) path.getType();
        if (nonNull(filter.getEquals())) {
            return path.eq(toEnum(type, filter.getEquals()));
        }
        if (nonNull(filter.getNotEquals())) {
            return path.ne(toEnum(type, filter.getNotEquals()));
        }
        if (!CollectionUtils.isEmpty(filter.getIn())) {
            List<T> enums = filter.getIn().stream().map(value -> toEnum(type, value)).toList();
            return path.in(enums);
        }
        if (!CollectionUtils.isEmpty(filter.getNotIn())) {
            List<T> enums = filter.getNotIn().stream().map(value -> toEnum(type, value)).toList();
            return path.notIn(enums);
        }
        return null;
    }

    private static <T extends Enum<T>> T toEnum(Class<T> enumClass, String value) {
        try {
            return Enum.valueOf(enumClass, value);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Невалидное значение enum %s: %s", enumClass, value));
        }
    }

    public static <T extends Comparable<? super T> & Serializable> BooleanExpression toComparablePath(RangeFilter<T> filter, ComparableExpression<T> path) {
        BooleanExpression simplePath = toSimplePath(filter, path);
        if (simplePath != null) {
            return simplePath;
        } else if (Objects.nonNull(filter.getGreaterThan()) && Objects.nonNull(filter.getLessThan())) {
            return path.gt(filter.getGreaterThan()).and(path.lt(filter.getLessThan()));
        } else if (Objects.nonNull(filter.getGreaterThan()) && Objects.nonNull(filter.getLessThanOrEqual())) {
            return path.gt(filter.getGreaterThan()).and(path.loe(filter.getLessThanOrEqual()));
        } else if (Objects.nonNull(filter.getGreaterThanOrEqual()) && Objects.nonNull(filter.getLessThan())) {
            return path.goe(filter.getGreaterThanOrEqual()).and(path.lt(filter.getLessThan()));
        } else if (Objects.nonNull(filter.getGreaterThanOrEqual()) && Objects.nonNull(filter.getLessThanOrEqual())) {
            return path.goe(filter.getGreaterThanOrEqual()).and(path.loe(filter.getLessThanOrEqual()));
        } else if (Objects.nonNull(filter.getGreaterThan())) {
            return path.gt(filter.getGreaterThan());
        } else if (Objects.nonNull(filter.getGreaterThanOrEqual())) {
            return path.goe(filter.getGreaterThanOrEqual());
        } else if (Objects.nonNull(filter.getLessThan())) {
            return path.lt(filter.getLessThan());
        } else {
            return Objects.nonNull(filter.getLessThanOrEqual()) ? path.loe(filter.getLessThanOrEqual()) : null;
        }
    }

    private static <T extends Serializable> BooleanExpression toSimplePath(Filter<T> filter, SimpleExpression<T> path) {
        if (Objects.nonNull(filter.getEquals())) {
            return path.eq(filter.getEquals());
        } else if (Objects.nonNull(filter.getNotEquals())) {
            return path.ne(filter.getNotEquals());
        } else if (!CollectionUtils.isEmpty(filter.getIn())) {
            return path.in(filter.getIn());
        } else {
            return !CollectionUtils.isEmpty(filter.getNotIn()) ? path.notIn(filter.getNotIn()) : null;
        }
    }

    public static BooleanExpression buildInstantPredicate(InstantFilter filter, DateTimePath<Instant> path) {
        if (nonNull(filter.getEquals())) {
            return path.eq(filter.getEquals());
        }
        if (nonNull(filter.getGreaterThan()) && nonNull(filter.getLessThan())) {
            return path.gt(filter.getGreaterThan()).and(path.lt(filter.getLessThan()));
        }
        if (nonNull(filter.getGreaterThanOrEqual()) && nonNull(filter.getLessThanOrEqual())) {
            return path.goe(filter.getGreaterThanOrEqual()).and(path.loe(filter.getLessThanOrEqual()));
        }
        if (nonNull(filter.getGreaterThanOrEqual()) && nonNull(filter.getLessThan())) {
            return path.goe(filter.getGreaterThanOrEqual()).and(path.lt(filter.getLessThan()));
        }
        if (nonNull(filter.getGreaterThan()) && nonNull(filter.getLessThanOrEqual())) {
            return path.gt(filter.getGreaterThan()).and(path.loe(filter.getLessThanOrEqual()));
        }
        if (nonNull(filter.getGreaterThan())) {
            return path.gt(filter.getGreaterThan());
        }
        if (nonNull(filter.getGreaterThanOrEqual())) {
            return path.goe(filter.getGreaterThanOrEqual());
        }
        if (nonNull(filter.getLessThan())) {
            return path.lt(filter.getLessThan());
        }
        if (nonNull(filter.getLessThanOrEqual())) {
            return path.loe(filter.getLessThanOrEqual());
        }
        if (!CollectionUtils.isEmpty(filter.getIn())) {
            return path.in(filter.getIn());
        }
        return null;
    }

    public static BooleanExpression append(BooleanExpression expression, BooleanExpression booleanExpression) {
        return Objects.isNull(expression) ? booleanExpression : expression.and(booleanExpression);
    }
}
