package com.evently.modules.ticketing.domain.tickets;

import com.evently.common.domain.IDomainEvent;

import java.util.UUID;

public record TicketArchivedDomainEvent(UUID ticketId, String code) implements IDomainEvent {
}