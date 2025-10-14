package com.evently.modules.ticketing.domain.orders;

import com.evently.common.domain.IDomainEvent;

import java.util.UUID;

public record OrderCreatedDomainEvent(UUID orderId) implements IDomainEvent {
}