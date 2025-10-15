package com.evently.common.infrastructure.outbox;

import com.evently.common.application.IEventBus;
import com.evently.common.domain.IDomainEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import org.springframework.context.annotation.Primary;

@Component
@Primary
public class OutboxEventBus implements IEventBus {

    private static final Logger logger = LoggerFactory.getLogger(OutboxEventBus.class);

    private final OutboxMessageRepository outboxMessageRepository;
    private final ObjectMapper objectMapper;

    public OutboxEventBus(OutboxMessageRepository outboxMessageRepository, ObjectMapper objectMapper) {
        this.outboxMessageRepository = outboxMessageRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public void publish(IDomainEvent domainEvent) {
        try {
            String content = objectMapper.writeValueAsString(domainEvent);
            String type = domainEvent.getClass().getSimpleName();

            OutboxMessage outboxMessage = new OutboxMessage(type, content, Instant.now());
            outboxMessageRepository.save(outboxMessage);

            logger.info("Stored domain event in outbox: {} - {}", type, domainEvent);
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize domain event: {}", domainEvent, e);
            throw new RuntimeException("Failed to serialize domain event", e);
        }
    }

    @Override
    @Transactional
    public void publishAll(IDomainEvent[] domainEvents) {
        for (IDomainEvent domainEvent : domainEvents) {
            publish(domainEvent);
        }
    }
}