package com.evently.modules.ticketing.domain.payments;

import com.evently.common.domain.IDomainEvent;

import java.util.UUID;

public record PaymentCreatedDomainEvent(UUID paymentId) implements IDomainEvent {
}