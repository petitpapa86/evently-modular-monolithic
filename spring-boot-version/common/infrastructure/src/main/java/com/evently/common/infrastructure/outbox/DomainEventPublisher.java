package com.evently.common.infrastructure.outbox;

import com.evently.common.application.IEventBus;
import com.evently.common.domain.IDomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.List;

@Component
public class DomainEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(DomainEventPublisher.class);

    private final IEventBus eventBus;

    public DomainEventPublisher(IEventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void publishEvents(List<IDomainEvent> domainEvents) {
        if (domainEvents.isEmpty()) {
            return;
        }

        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            // If we're in a transaction, register a synchronization to publish events after commit
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    publishEventsAfterCommit(domainEvents);
                }
            });
        } else {
            // If not in a transaction, publish immediately
            publishEventsAfterCommit(domainEvents);
        }
    }

    private void publishEventsAfterCommit(List<IDomainEvent> domainEvents) {
        try {
            logger.info("Publishing {} domain events after transaction commit", domainEvents.size());
            eventBus.publishAll(domainEvents.toArray(new IDomainEvent[0]));
        } catch (Exception e) {
            logger.error("Failed to publish domain events after transaction commit", e);
            // In a production system, you might want to implement retry logic or dead letter queue
        }
    }
}