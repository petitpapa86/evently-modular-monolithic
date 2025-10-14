package com.evently.common.domain;

import java.time.Instant;
import java.util.UUID;

public abstract class DomainEvent implements IDomainEvent {
    private final UUID id;
    private final Instant occurredOnUtc;

    protected DomainEvent() {
        this.id = UUID.randomUUID();
        this.occurredOnUtc = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public Instant getOccurredOnUtc() {
        return occurredOnUtc;
    }
}