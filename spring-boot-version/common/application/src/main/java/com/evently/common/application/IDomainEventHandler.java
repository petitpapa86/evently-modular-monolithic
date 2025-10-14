package com.evently.common.application;

import com.evently.common.domain.IDomainEvent;

public interface IDomainEventHandler<T extends IDomainEvent> {
    void handle(T event);
    Class<T> getEventType();
}