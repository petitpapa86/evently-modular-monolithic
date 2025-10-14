package com.evently.modules.ticketing.domain.events;

import com.evently.common.domain.IDomainEvent;

import java.util.UUID;

public record EventPaymentsRefundedDomainEvent(UUID eventId) implements IDomainEvent {
}