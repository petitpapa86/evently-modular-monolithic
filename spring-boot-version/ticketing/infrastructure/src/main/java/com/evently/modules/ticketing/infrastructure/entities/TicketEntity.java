package com.evently.modules.ticketing.infrastructure.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tickets")
public class TicketEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID customerId;

    @Column(nullable = false)
    private UUID orderId;

    @Column(nullable = false)
    private UUID eventId;

    @Column(nullable = false)
    private UUID ticketTypeId;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private LocalDateTime createdAtUtc;

    @Column(nullable = false)
    private boolean archived;

    protected TicketEntity() {
    }

    public TicketEntity(UUID id, UUID customerId, UUID orderId, UUID eventId, UUID ticketTypeId,
                       String code, LocalDateTime createdAtUtc, boolean archived) {
        this.id = id;
        this.customerId = customerId;
        this.orderId = orderId;
        this.eventId = eventId;
        this.ticketTypeId = ticketTypeId;
        this.code = code;
        this.createdAtUtc = createdAtUtc;
        this.archived = archived;
    }

    // Getters
    public UUID getId() { return id; }
    public UUID getCustomerId() { return customerId; }
    public UUID getOrderId() { return orderId; }
    public UUID getEventId() { return eventId; }
    public UUID getTicketTypeId() { return ticketTypeId; }
    public String getCode() { return code; }
    public LocalDateTime getCreatedAtUtc() { return createdAtUtc; }
    public boolean isArchived() { return archived; }

    // Setters for updates
    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}