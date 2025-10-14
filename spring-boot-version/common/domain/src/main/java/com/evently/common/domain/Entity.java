package com.evently.common.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Entity {
    private final List<IDomainEvent> domainEvents = new ArrayList<>();

    protected Entity() {
    }

    public List<IDomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

    protected void raise(IDomainEvent domainEvent) {
        domainEvents.add(domainEvent);
    }
}