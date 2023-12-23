package com.offsidegaming.measurer.service.dto.filter;

import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
public class LocalDateFilter extends RangeFilter<LocalDate> {
    @Serial
    private static final long serialVersionUID = 1L;

    public LocalDateFilter(final LocalDateFilter filter) {
        super(filter);
    }

    public static LocalDateFilter eq(LocalDate equals) {
        return new LocalDateFilter().setEquals(equals);
    }

    public static LocalDateFilter notEq(LocalDate equals) {
        return new LocalDateFilter().setNotEquals(equals);
    }

    public static LocalDateFilter lt(LocalDate value) {
        return new LocalDateFilter().setLessThan(value);
    }

    public static LocalDateFilter lte(LocalDate value) {
        return new LocalDateFilter().setLessThanOrEqual(value);
    }

    public static LocalDateFilter gt(LocalDate value) {
        return new LocalDateFilter().setGreaterThan(value);
    }

    public static LocalDateFilter gte(LocalDate value) {
        return new LocalDateFilter().setGreaterThanOrEqual(value);
    }

    public static LocalDateFilter in(LocalDate... values) {
        return new LocalDateFilter().setIn(Arrays.asList(values));
    }

    public static LocalDateFilter in(List<LocalDate> values) {
        return new LocalDateFilter().setIn(values);
    }

    public static LocalDateFilter notIn(List<LocalDate> values) {
        return new LocalDateFilter().setNotIn(values);
    }

    public static LocalDateFilter notIn(LocalDate... values) {
        return new LocalDateFilter().setNotIn(Arrays.asList(values));
    }

    @Override
    public LocalDateFilter copy() {
        return new LocalDateFilter(this);
    }

    @Override
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    public LocalDateFilter setEquals(LocalDate equals) {
        super.setEquals(equals);
        return this;
    }

    @Override
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    public LocalDateFilter setNotEquals(LocalDate equals) {
        super.setNotEquals(equals);
        return this;
    }

    @Override
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    public LocalDateFilter setIn(List<LocalDate> in) {
        super.setIn(in);
        return this;
    }

    @Override
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    public LocalDateFilter setNotIn(List<LocalDate> notIn) {
        super.setNotIn(notIn);
        return this;
    }

    @Override
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    public LocalDateFilter setGreaterThan(LocalDate equals) {
        super.setGreaterThan(equals);
        return this;
    }

    @Override
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    public LocalDateFilter setLessThan(LocalDate equals) {
        super.setLessThan(equals);
        return this;
    }

    @Override
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    public LocalDateFilter setGreaterThanOrEqual(LocalDate equals) {
        super.setGreaterThanOrEqual(equals);
        return this;
    }

    @Override
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    public LocalDateFilter setLessThanOrEqual(LocalDate equals) {
        super.setLessThanOrEqual(equals);
        return this;
    }
}
