package com.evently.modules.ticketing.infrastructure.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "TicketingTicketEntity")
@Table(name = "tickets", schema = "ticketing")
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

    // Domain mapping methods
    public static TicketEntity fromDomain(com.evently.modules.ticketing.domain.tickets.Ticket ticket) {
        return new TicketEntity(
                ticket.getId(),
                ticket.getCustomerId(),
                ticket.getOrderId(),
                ticket.getEventId(),
                ticket.getTicketTypeId(),
                ticket.getCode(),
                ticket.getCreatedAtUtc(),
                ticket.isArchived()
        );
    }

    public com.evently.modules.ticketing.domain.tickets.Ticket toDomain() {
        // Note: This is a simplified mapping. In a real implementation,
        // you would need to reconstruct the full domain object properly
        // This might require additional repository calls to get related entities
        try {
            var ticketClass = Class.forName("com.evently.modules.ticketing.domain.tickets.Ticket");
            var ticket = ticketClass.getDeclaredConstructor().newInstance();
            var idField = ticketClass.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(ticket, id);

            var customerIdField = ticketClass.getDeclaredField("customerId");
            customerIdField.setAccessible(true);
            customerIdField.set(ticket, customerId);

            var orderIdField = ticketClass.getDeclaredField("orderId");
            orderIdField.setAccessible(true);
            orderIdField.set(ticket, orderId);

            var eventIdField = ticketClass.getDeclaredField("eventId");
            eventIdField.setAccessible(true);
            eventIdField.set(ticket, eventId);

            var ticketTypeIdField = ticketClass.getDeclaredField("ticketTypeId");
            ticketTypeIdField.setAccessible(true);
            ticketTypeIdField.set(ticket, ticketTypeId);

            var codeField = ticketClass.getDeclaredField("code");
            codeField.setAccessible(true);
            codeField.set(ticket, code);

            var createdAtUtcField = ticketClass.getDeclaredField("createdAtUtc");
            createdAtUtcField.setAccessible(true);
            createdAtUtcField.set(ticket, createdAtUtc);

            var archivedField = ticketClass.getDeclaredField("archived");
            archivedField.setAccessible(true);
            archivedField.set(ticket, archived);

            return (com.evently.modules.ticketing.domain.tickets.Ticket) ticket;
        } catch (Exception e) {
            throw new RuntimeException("Failed to map TicketEntity to domain", e);
        }
    }
}