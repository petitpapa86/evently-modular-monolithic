package com.evently.common.infrastructure.inbox;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "inbox_message_consumers", schema = "evently")
public class InboxMessageConsumer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "inbox_message_id", nullable = false)
    private UUID inboxMessageId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "processed_at", nullable = false)
    private LocalDateTime processedAt;

    protected InboxMessageConsumer() {
        // JPA default constructor
    }

    public InboxMessageConsumer(UUID inboxMessageId, String name) {
        this.inboxMessageId = inboxMessageId;
        this.name = name;
        this.processedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getInboxMessageId() {
        return inboxMessageId;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }
}