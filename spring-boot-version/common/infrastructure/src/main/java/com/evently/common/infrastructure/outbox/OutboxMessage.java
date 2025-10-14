package com.evently.common.infrastructure.outbox;

import jakarta.persistence.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_messages")
public class OutboxMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "occurred_on_utc", nullable = false)
    private Instant occurredOnUtc;

    @Column(name = "processed_on_utc")
    private Instant processedOnUtc;

    @Column(name = "error")
    private String error;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OutboxMessageStatus status;

    protected OutboxMessage() {
        // JPA default constructor
    }

    public OutboxMessage(String type, String content, Instant occurredOnUtc) {
        this.type = type;
        this.content = content;
        this.occurredOnUtc = occurredOnUtc;
        this.status = OutboxMessageStatus.PENDING;
    }

    public void markAsProcessed() {
        this.status = OutboxMessageStatus.PROCESSED;
        this.processedOnUtc = Instant.now();
    }

    public void markAsFailed(String error) {
        this.status = OutboxMessageStatus.FAILED;
        this.error = error;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public Instant getOccurredOnUtc() {
        return occurredOnUtc;
    }

    public Instant getProcessedOnUtc() {
        return processedOnUtc;
    }

    public String getError() {
        return error;
    }

    public OutboxMessageStatus getStatus() {
        return status;
    }
}