package com.evently.modules.ticketing.domain.orders;

import com.evently.common.domain.IDomainEvent;

import java.util.UUID;

public record OrderTicketsIssuedDomainEvent(UUID orderId) implements IDomainEvent {
}