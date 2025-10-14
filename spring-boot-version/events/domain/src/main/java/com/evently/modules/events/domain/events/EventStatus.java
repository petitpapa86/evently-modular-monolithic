package com.evently.modules.events.domain.events;

public enum EventStatus {
    DRAFT(0),
    PUBLISHED(1),
    COMPLETED(2),
    CANCELED(3);

    private final int value;

    EventStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}