package com.evently.modules.ticketing.domain.tickets;

import com.evently.common.domain.Entity;
import com.evently.common.domain.IDomainEvent;
import com.evently.modules.ticketing.domain.events.TicketType;
import com.evently.modules.ticketing.domain.orders.Order;

import java.time.LocalDateTime;
import java.util.UUID;

public class Ticket extends Entity {

    private Ticket() {
    }

    private UUID id;
    private UUID customerId;
    private UUID orderId;
    private UUID eventId;
    private UUID ticketTypeId;
    private String code;
    private LocalDateTime createdAtUtc;
    private boolean archived;

    public static Ticket create(Order order, TicketType ticketType) {
        Ticket ticket = new Ticket();
        ticket.id = UUID.randomUUID();
        ticket.customerId = order.getCustomerId();
        ticket.orderId = order.getId();
        ticket.eventId = ticketType.getEventId();
        ticket.ticketTypeId = ticketType.getId();
        ticket.code = "tc_" + generateUlid();
        ticket.createdAtUtc = LocalDateTime.now();
        ticket.archived = false;
        ticket.raise(new TicketCreatedDomainEvent(ticket.id));
        return ticket;
    }

    public void archive() {
        if (archived) {
            return;
        }

        archived = true;
        raise(new TicketArchivedDomainEvent(id, code));
    }

    private static String generateUlid() {
        // Simple ULID-like generation for now
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
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
}