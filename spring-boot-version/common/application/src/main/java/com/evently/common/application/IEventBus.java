package com.evently.common.application;

import com.evently.common.domain.IDomainEvent;

public interface IEventBus {
    void publish(IDomainEvent domainEvent);
    void publishAll(IDomainEvent[] domainEvents);
}