package com.evently.common.infrastructure.outbox;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class OutboxProcessor {

    private static final Logger logger = LoggerFactory.getLogger(OutboxProcessor.class);
    private static final int BATCH_SIZE = 10;

    private final OutboxMessageRepository outboxMessageRepository;
    private final ObjectMapper objectMapper;

    public OutboxProcessor(OutboxMessageRepository outboxMessageRepository, ObjectMapper objectMapper) {
        this.outboxMessageRepository = outboxMessageRepository;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelay = 5000) // Run every 5 seconds
    @Transactional
    public void processOutboxMessages() {
        List<OutboxMessage> pendingMessages = outboxMessageRepository
            .findByStatusOrderByOccurredOnUtc(OutboxMessageStatus.PENDING);

        if (pendingMessages.isEmpty()) {
            return;
        }

        logger.info("Processing {} outbox messages", pendingMessages.size());

        int processedCount = 0;
        for (OutboxMessage message : pendingMessages) {
            if (processedCount >= BATCH_SIZE) {
                break; // Process in batches to avoid long-running transactions
            }

            try {
                publishMessage(message);
                message.markAsProcessed();
                processedCount++;
            } catch (Exception e) {
                logger.error("Failed to process outbox message {}: {}", message.getId(), e.getMessage());
                message.markAsFailed(e.getMessage());
            }
        }

        outboxMessageRepository.saveAll(pendingMessages.subList(0, processedCount));
        logger.info("Processed {} outbox messages successfully", processedCount);
    }

    private void publishMessage(OutboxMessage message) {
        try {
            // Parse the message content
            JsonNode content = objectMapper.readTree(message.getContent());

            // In a real implementation, this would publish to a message broker like RabbitMQ, Kafka, etc.
            // For now, just log the message
            logger.info("Publishing outbox message: type={}, content={}",
                       message.getType(), content.toString());

            // Simulate publishing to external system
            // messageBroker.publish(message.getType(), content);

        } catch (Exception e) {
            logger.error("Failed to publish message {}: {}", message.getId(), e.getMessage());
            throw new RuntimeException("Failed to publish message", e);
        }
    }
}