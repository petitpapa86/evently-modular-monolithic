package com.evently.common.domain;

public record Error(String code, String description, ErrorType type) {
    public static final Error NONE = new Error("", "", ErrorType.FAILURE);
    public static final Error NULL_VALUE = new Error("General.Null", "Null value was provided", ErrorType.FAILURE);

    public static Error failure(String code, String description) {
        return new Error(code, description, ErrorType.FAILURE);
    }

    public static Error notFound(String code, String description) {
        return new Error(code, description, ErrorType.NOT_FOUND);
    }

    public static Error problem(String code, String description) {
        return new Error(code, description, ErrorType.PROBLEM);
    }

    public static Error conflict(String code, String description) {
        return new Error(code, description, ErrorType.CONFLICT);
    }
}