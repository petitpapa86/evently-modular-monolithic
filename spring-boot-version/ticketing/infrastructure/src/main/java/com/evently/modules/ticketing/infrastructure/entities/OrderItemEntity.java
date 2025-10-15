package com.evently.modules.ticketing.infrastructure.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_items", schema = "ticketing")
public class OrderItemEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @Column(nullable = false)
    private UUID ticketTypeId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private String currency;

    protected OrderItemEntity() {
    }

    public OrderItemEntity(UUID id, OrderEntity order, UUID ticketTypeId, BigDecimal quantity,
                          BigDecimal unitPrice, String currency) {
        this.id = id;
        this.order = order;
        this.ticketTypeId = ticketTypeId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.currency = currency;
    }

    // Getters
    public UUID getId() { return id; }
    public OrderEntity getOrder() { return order; }
    public UUID getTicketTypeId() { return ticketTypeId; }
    public BigDecimal getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public String getCurrency() { return currency; }
}