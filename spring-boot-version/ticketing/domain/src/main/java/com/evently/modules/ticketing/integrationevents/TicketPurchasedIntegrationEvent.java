package com.evently.modules.ticketing.integrationevents;

import com.evently.common.application.IntegrationEvent;

import java.util.UUID;

public class TicketPurchasedIntegrationEvent extends IntegrationEvent {

    private final UUID ticketId;
    private final UUID customerId;
    private final UUID eventId;
    private final String ticketCode;

    public TicketPurchasedIntegrationEvent(UUID ticketId, UUID customerId, UUID eventId, String ticketCode) {
        this.ticketId = ticketId;
        this.customerId = customerId;
        this.eventId = eventId;
        this.ticketCode = ticketCode;
    }

    public UUID getTicketId() {
        return ticketId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public UUID getEventId() {
        return eventId;
    }

    public String getTicketCode() {
        return ticketCode;
    }
}