package com.offsidegaming.measurer.service.dto.filter;

import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
public class InstantFilter extends RangeFilter<Instant> {

    @Serial
    private static final long serialVersionUID = 1L;

    public InstantFilter(final InstantFilter filter) {
        super(filter);
    }

    public static InstantFilter eq(Instant equals) {
        return new InstantFilter().setEquals(equals);
    }

    public static InstantFilter notEq(Instant equals) {
        return new InstantFilter().setNotEquals(equals);
    }

    public static InstantFilter lt(Instant value) {
        return new InstantFilter().setLessThan(value);
    }

    public static InstantFilter lte(Instant value) {
        return new InstantFilter().setLessThanOrEqual(value);
    }

    public static InstantFilter gt(Instant value) {
        return new InstantFilter().setGreaterThan(value);
    }

    public static InstantFilter gte(Instant value) {
        return new InstantFilter().setGreaterThanOrEqual(value);
    }

    public static InstantFilter in(Instant... values) {
        return new InstantFilter().setIn(Arrays.asList(values));
    }

    public static InstantFilter in(List<Instant> values) {
        return new InstantFilter().setIn(values);
    }

    public static InstantFilter notIn(List<Instant> values) {
        return new InstantFilter().setNotIn(values);
    }

    public static InstantFilter notIn(Instant... values) {
        return new InstantFilter().setNotIn(Arrays.asList(values));
    }

    @Override
    public InstantFilter copy() {
        return new InstantFilter(this);
    }

    @Override
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public InstantFilter setEquals(Instant equals) {
        super.setEquals(equals);
        return this;
    }

    @Override
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public InstantFilter setNotEquals(Instant equals) {
        super.setNotEquals(equals);
        return this;
    }

    @Override
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public InstantFilter setIn(List<Instant> in) {
        super.setIn(in);
        return this;
    }

    @Override
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public InstantFilter setNotIn(List<Instant> notIn) {
        super.setNotIn(notIn);
        return this;
    }

    @Override
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public InstantFilter setGreaterThan(Instant equals) {
        super.setGreaterThan(equals);
        return this;
    }

    @Override
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public InstantFilter setLessThan(Instant equals) {
        super.setLessThan(equals);
        return this;
    }

    @Override
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public InstantFilter setGreaterThanOrEqual(Instant equals) {
        super.setGreaterThanOrEqual(equals);
        return this;
    }

    @Override
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public InstantFilter setLessThanOrEqual(Instant equals) {
        super.setLessThanOrEqual(equals);
        return this;
    }
}
