package com.evently.modules.ticketing.domain.payments;

import com.evently.common.domain.IDomainEvent;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRefundedDomainEvent(UUID paymentId, UUID transactionId, BigDecimal refundAmount)
    implements IDomainEvent {
}