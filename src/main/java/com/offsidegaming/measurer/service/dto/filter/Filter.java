package com.offsidegaming.measurer.service.dto.filter;

import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class Filter<T extends Serializable> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private T equals;
    private T notEquals;
    private List<T> in;
    private List<T> notIn;

    public Filter(Filter<T> filter) {
        this.equals = filter.equals;
        this.notEquals = filter.notEquals;
        this.in = filter.in == null ? null : new ArrayList<>(filter.in);
        this.notIn = filter.notIn == null ? null : new ArrayList<>(filter.notIn);
    }

    public Filter<T> copy() {
        return new Filter<>(this);
    }

    public T getEquals() {
        return equals;
    }

    public Filter<T> setEquals(T equals) {
        this.equals = equals;
        return this;
    }

    public T getNotEquals() {
        return notEquals;
    }

    public Filter<T> setNotEquals(T notEquals) {
        this.notEquals = notEquals;
        return this;
    }

    public List<T> getIn() {
        return in;
    }

    public Filter<T> setIn(List<T> in) {
        this.in = in;
        return this;
    }

    public List<T> getNotIn() {
        return notIn;
    }

    public Filter<T> setNotIn(List<T> notIn) {
        this.notIn = notIn;
        return this;
    }

    protected String getFilterName() {
        return this.getClass().getSimpleName();
    }
}
