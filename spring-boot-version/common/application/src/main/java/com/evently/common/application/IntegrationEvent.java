package com.evently.common.application;

import java.time.Instant;
import java.util.UUID;

public abstract class IntegrationEvent {
    private final UUID id;
    private final Instant occurredOnUtc;

    protected IntegrationEvent() {
        this.id = UUID.randomUUID();
        this.occurredOnUtc = Instant.now();
    }

    protected IntegrationEvent(UUID id, Instant occurredOnUtc) {
        this.id = id;
        this.occurredOnUtc = occurredOnUtc;
    }

    public UUID getId() {
        return id;
    }

    public Instant getOccurredOnUtc() {
        return occurredOnUtc;
    }
}