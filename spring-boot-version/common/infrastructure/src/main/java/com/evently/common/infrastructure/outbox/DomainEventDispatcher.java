package com.evently.common.infrastructure.outbox;

import com.evently.common.application.IDomainEventHandler;
import com.evently.common.domain.IDomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DomainEventDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(DomainEventDispatcher.class);

    private final Map<Class<? extends IDomainEvent>, List<IDomainEventHandler<? extends IDomainEvent>>> handlers = new ConcurrentHashMap<>();

    public void registerHandler(IDomainEventHandler<? extends IDomainEvent> handler) {
        Class<? extends IDomainEvent> eventType = handler.getEventType();
        handlers.computeIfAbsent(eventType, k -> new java.util.ArrayList<>()).add(handler);
        logger.info("Registered event handler {} for event type {}", handler.getClass().getSimpleName(), eventType.getSimpleName());
    }

    public void dispatch(IDomainEvent event) {
        List<IDomainEventHandler<? extends IDomainEvent>> eventHandlers = handlers.get(event.getClass());
        if (eventHandlers != null) {
            for (IDomainEventHandler<? extends IDomainEvent> handler : eventHandlers) {
                try {
                    @SuppressWarnings("unchecked")
                    IDomainEventHandler<IDomainEvent> typedHandler = (IDomainEventHandler<IDomainEvent>) handler;
                    typedHandler.handle(event);
                    logger.info("Dispatched event {} to handler {}", event.getClass().getSimpleName(), handler.getClass().getSimpleName());
                } catch (Exception e) {
                    logger.error("Failed to handle event {} with handler {}", event.getClass().getSimpleName(), handler.getClass().getSimpleName(), e);
                }
            }
        }
    }
}