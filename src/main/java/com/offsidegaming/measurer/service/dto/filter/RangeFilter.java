package com.offsidegaming.measurer.service.dto.filter;

import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
public class RangeFilter<T extends Comparable<? super T> & Serializable> extends Filter<T> {

    @Serial
    private static final long serialVersionUID = 1L;

    private T greaterThan;
    private T lessThan;
    private T greaterThanOrEqual;
    private T lessThanOrEqual;

    public RangeFilter(final RangeFilter<T> filter) {
        super(filter);
        this.greaterThan = filter.greaterThan;
        this.lessThan = filter.lessThan;
        this.greaterThanOrEqual = filter.greaterThanOrEqual;
        this.lessThanOrEqual = filter.lessThanOrEqual;
    }

    @Override
    public RangeFilter<T> copy() {
        return new RangeFilter<>(this);
    }

    public T getGreaterThan() {
        return greaterThan;
    }

    public RangeFilter<T> setGreaterThan(T greaterThan) {
        this.greaterThan = greaterThan;
        return this;
    }

    public T getLessThan() {
        return lessThan;
    }

    public RangeFilter<T> setLessThan(T lessThan) {
        this.lessThan = lessThan;
        return this;
    }

    public T getGreaterThanOrEqual() {
        return greaterThanOrEqual;
    }

    public RangeFilter<T> setGreaterThanOrEqual(T greaterThanOrEqual) {
        this.greaterThanOrEqual = greaterThanOrEqual;
        return this;
    }

    public T getLessThanOrEqual() {
        return lessThanOrEqual;
    }

    public RangeFilter<T> setLessThanOrEqual(T lessThanOrEqual) {
        this.lessThanOrEqual = lessThanOrEqual;
        return this;
    }
}
