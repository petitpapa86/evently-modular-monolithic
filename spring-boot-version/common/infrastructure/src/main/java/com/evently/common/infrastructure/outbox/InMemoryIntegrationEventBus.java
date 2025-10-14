package com.evently.common.infrastructure.outbox;

import com.evently.common.application.IIntegrationEventBus;
import com.evently.common.application.IntegrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class InMemoryIntegrationEventBus implements IIntegrationEventBus {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryIntegrationEventBus.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    public InMemoryIntegrationEventBus(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(IntegrationEvent integrationEvent) {
        logger.info("Publishing integration event: {} - {}", integrationEvent.getClass().getSimpleName(), integrationEvent);
        // Publish using Spring's ApplicationEventPublisher for in-memory event handling
        applicationEventPublisher.publishEvent(integrationEvent);
    }
}