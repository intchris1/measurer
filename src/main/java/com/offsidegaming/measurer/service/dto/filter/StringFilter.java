package com.offsidegaming.measurer.service.dto.filter;

import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
public class StringFilter extends Filter<String> {

    @Serial
    private static final long serialVersionUID = 1L;

    private String like;
    private String contains;
    private String doesNotContain;

    public StringFilter(final StringFilter filter) {
        super(filter);
        this.contains = filter.contains;
        this.doesNotContain = filter.doesNotContain;
        this.like = filter.like;
    }

    public static StringFilter eq(String equals) {
        return new StringFilter().setEquals(equals);
    }

    public static StringFilter notEq(String equals) {
        return new StringFilter().setNotEquals(equals);
    }

    public static StringFilter contains(String contains) {
        return new StringFilter().setContains(contains);
    }

    public static StringFilter in(String... values) {
        return new StringFilter().setIn(Arrays.asList(values));
    }

    public static StringFilter in(List<String> values) {
        return new StringFilter().setIn(values);
    }

    public static StringFilter notIn(List<String> values) {
        return new StringFilter().setNotIn(values);
    }

    public static StringFilter notIn(String... values) {
        return new StringFilter().setNotIn(Arrays.asList(values));
    }

    @Override
    public StringFilter copy() {
        return new StringFilter(this);
    }

    public String getContains() {
        return contains;
    }

    public StringFilter setContains(String contains) {
        this.contains = contains;
        return this;
    }

    public String getDoesNotContain() {
        return doesNotContain;
    }

    public StringFilter setDoesNotContain(String doesNotContain) {
        this.doesNotContain = doesNotContain;
        return this;
    }

    public String getLike() {
        return like;
    }

    public StringFilter setLike(String like) {
        this.like = like;
        return this;
    }

    @Override
    public StringFilter setEquals(String equals) {
        super.setEquals(equals);
        return this;
    }

    @Override
    public StringFilter setNotEquals(String notEquals) {
        super.setNotEquals(notEquals);
        return this;
    }

    @Override
    public StringFilter setIn(List<String> in) {
        super.setIn(in);
        return this;
    }

    @Override
    public StringFilter setNotIn(List<String> notIn) {
        super.setNotIn(notIn);
        return this;
    }
}
