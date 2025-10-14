package com.evently.common.infrastructure.outbox;

import com.evently.common.application.IEventBus;
import com.evently.common.domain.IDomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InMemoryEventBus implements IEventBus {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryEventBus.class);

    @Override
    public void publish(IDomainEvent domainEvent) {
        logger.info("Publishing domain event: {} - {}", domainEvent.getClass().getSimpleName(), domainEvent);
        // In a real implementation, this would publish to a message broker
        // For now, just log the event
    }

    @Override
    public void publishAll(IDomainEvent[] domainEvents) {
        for (IDomainEvent domainEvent : domainEvents) {
            publish(domainEvent);
        }
    }
}