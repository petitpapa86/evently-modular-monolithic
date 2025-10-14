package com.evently.modules.ticketing.domain.orders;

public enum OrderStatus {
    PENDING(0),
    PAID(1),
    REFUNDED(2),
    CANCELED(3);

    private final int value;

    OrderStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}