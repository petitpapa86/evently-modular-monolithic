package com.evently.common.domain;

public record ValidationError(String propertyName, String errorMessage) {
}