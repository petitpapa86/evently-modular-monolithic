package com.evently.modules.events.domain.tickettypes;

import com.evently.common.domain.DomainEvent;
import java.util.UUID;

public class TicketTypeCreatedDomainEvent extends DomainEvent {
    private final UUID ticketTypeId;

    public TicketTypeCreatedDomainEvent(UUID ticketTypeId) {
        this.ticketTypeId = ticketTypeId;
    }

    public UUID getTicketTypeId() {
        return ticketTypeId;
    }
}