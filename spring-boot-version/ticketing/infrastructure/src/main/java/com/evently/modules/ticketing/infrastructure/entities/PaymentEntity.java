package com.evently.modules.ticketing.infrastructure.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments", schema = "ticketing")
public class PaymentEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID orderId;

    @Column(nullable = false)
    private UUID transactionId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amountRefunded;

    @Column(nullable = false)
    private LocalDateTime createdAtUtc;

    @Column
    private LocalDateTime refundedAtUtc;

    protected PaymentEntity() {
    }

    public PaymentEntity(UUID id, UUID orderId, UUID transactionId, BigDecimal amount,
                        String currency, BigDecimal amountRefunded, LocalDateTime createdAtUtc,
                        LocalDateTime refundedAtUtc) {
        this.id = id;
        this.orderId = orderId;
        this.transactionId = transactionId;
        this.amount = amount;
        this.currency = currency;
        this.amountRefunded = amountRefunded;
        this.createdAtUtc = createdAtUtc;
        this.refundedAtUtc = refundedAtUtc;
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

    // Setters for updates
    public void setAmountRefunded(BigDecimal amountRefunded) {
        this.amountRefunded = amountRefunded;
    }

    public void setRefundedAtUtc(LocalDateTime refundedAtUtc) {
        this.refundedAtUtc = refundedAtUtc;
    }
}