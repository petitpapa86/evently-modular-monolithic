package com.evently.modules.ticketing.domain.payments;

import com.evently.common.domain.Entity;
import com.evently.common.domain.IDomainEvent;
import com.evently.common.domain.Result;
import com.evently.modules.ticketing.domain.orders.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Payment extends Entity {

    private Payment() {
    }

    private UUID id;
    private UUID orderId;
    private UUID transactionId;
    private BigDecimal amount;
    private String currency;
    private BigDecimal amountRefunded;
    private LocalDateTime createdAtUtc;
    private LocalDateTime refundedAtUtc;

    public static Payment create(Order order, UUID transactionId, BigDecimal amount, String currency) {
        Payment payment = new Payment();
        payment.id = UUID.randomUUID();
        payment.orderId = order.getId();
        payment.transactionId = transactionId;
        payment.amount = amount;
        payment.currency = currency;
        payment.amountRefunded = BigDecimal.ZERO;
        payment.createdAtUtc = LocalDateTime.now();
        payment.raise(new PaymentCreatedDomainEvent(payment.id));
        return payment;
    }

    public Result refund(BigDecimal refundAmount) {
        if (amountRefunded.compareTo(amount) == 0) {
            return Result.failure(PaymentErrors.alreadyRefunded());
        }

        if (amountRefunded.add(refundAmount).compareTo(amount) > 0) {
            return Result.failure(PaymentErrors.notEnoughFunds());
        }

        amountRefunded = amountRefunded.add(refundAmount);

        if (amount.compareTo(amountRefunded) == 0) {
            raise(new PaymentRefundedDomainEvent(id, transactionId, refundAmount));
        } else {
            raise(new PaymentPartiallyRefundedDomainEvent(id, transactionId, refundAmount));
        }

        return Result.success();
    }

    // Getters
    public UUID getId() { return id; }
    public UUID getOrderId() { return orderId; }
    public UUID getTransactionId() { return transactionId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public BigDecimal getAmountRefunded() { return amountRefunded; }
    public LocalDateTime getCreatedAtUtc() { return createdAtUtc; }
    public LocalDateTime getRefundedAtUtc() { return refundedAtUtc; }
}