package com.evently.modules.ticketing.infrastructure.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID customerId;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private LocalDateTime createdAtUtc;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    protected OrderEntity() {
    }

    public OrderEntity(UUID id, UUID customerId, String status, BigDecimal totalPrice,
                      String currency, LocalDateTime createdAtUtc) {
        this.id = id;
        this.customerId = customerId;
        this.status = status;
        this.totalPrice = totalPrice;
        this.currency = currency;
        this.createdAtUtc = createdAtUtc;
    }

    // Getters
    public UUID getId() { return id; }
    public UUID getCustomerId() { return customerId; }
    public String getStatus() { return status; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public String getCurrency() { return currency; }
    public LocalDateTime getCreatedAtUtc() { return createdAtUtc; }
    public List<OrderItemEntity> getOrderItems() { return orderItems; }

    // Setters for updates
    public void setStatus(String status) {
        this.status = status;
    }

    public void setOrderItems(List<OrderItemEntity> orderItems) {
        this.orderItems = orderItems;
    }
}