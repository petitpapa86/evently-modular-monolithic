package com.evently.common.domain;

import java.util.List;

public class Result<T> {
    private final boolean isSuccess;
    private final T value;
    private final List<Error> errors;

    private Result(boolean isSuccess, T value, List<Error> errors) {
        this.isSuccess = isSuccess;
        this.value = value;
        this.errors = errors;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(true, value, List.of());
    }

    public static <T> Result<T> success() {
        return new Result<>(true, null, List.of());
    }

    public static <T> Result<T> failure(List<Error> errors) {
        return new Result<>(false, null, errors);
    }

    public static <T> Result<T> failure(Error error) {
        return new Result<>(false, null, List.of(error));
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isFailure() {
        return !isSuccess;
    }

    public T getValue() {
        return value;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public <U> U match(java.util.function.Function<T, U> onSuccess, java.util.function.Supplier<U> onFailure) {
        return isSuccess ? onSuccess.apply(value) : onFailure.get();
    }
}