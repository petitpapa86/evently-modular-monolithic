package com.evently.common.infrastructure.inbox;

import com.evently.common.application.IIntegrationEventBus;
import com.evently.common.application.IIntegrationEventHandler;
import com.evently.common.application.IntegrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryIntegrationEventBus implements IIntegrationEventBus {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryIntegrationEventBus.class);

    private final ApplicationEventPublisher applicationEventPublisher;
    private final Map<Class<?>, List<IIntegrationEventHandler<?>>> handlers;

    public InMemoryIntegrationEventBus(
            ApplicationEventPublisher applicationEventPublisher,
            List<IIntegrationEventHandler<?>> handlers) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.handlers = new ConcurrentHashMap<>();

        // Group handlers by event type
        for (IIntegrationEventHandler<?> handler : handlers) {
            Class<?> eventType = handler.getEventType();
            this.handlers.computeIfAbsent(eventType, k -> new java.util.ArrayList<>()).add(handler);
        }

        logger.info("Registered {} integration event handlers for {} event types",
                   handlers.size(), this.handlers.size());
    }

    @Override
    public void publish(IntegrationEvent integrationEvent) {
        logger.info("Publishing integration event: {} ({})",
                   integrationEvent.getClass().getSimpleName(), integrationEvent.getId());

        List<IIntegrationEventHandler<?>> eventHandlers = handlers.get(integrationEvent.getClass());

        if (eventHandlers == null || eventHandlers.isEmpty()) {
            logger.warn("No handlers found for integration event: {}", integrationEvent.getClass().getSimpleName());
            return;
        }

        // Publish to all handlers for this event type
        for (IIntegrationEventHandler<?> handler : eventHandlers) {
            try {
                // The handler is already wrapped with IdempotentIntegrationEventHandler via BeanPostProcessor
                @SuppressWarnings("unchecked")
                IIntegrationEventHandler<IntegrationEvent> typedHandler =
                    (IIntegrationEventHandler<IntegrationEvent>) handler;
                typedHandler.handle(integrationEvent);
            } catch (Exception e) {
                logger.error("Error handling integration event {} with handler {}",
                           integrationEvent.getId(), handler.getClass().getSimpleName(), e);
                // Continue with other handlers even if one fails
            }
        }
    }
}