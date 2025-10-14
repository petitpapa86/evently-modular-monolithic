package com.evently.modules.ticketing.domain.events;

import com.evently.common.domain.IDomainEvent;

import java.util.UUID;

public record EventTicketsArchivedDomainEvent(UUID eventId) implements IDomainEvent {
}