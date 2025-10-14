package com.evently.modules.ticketing.infrastructure.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity(name = "TicketingTicketTypeEntity")
@Table(name = "ticket_types")
public class TicketTypeEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID eventId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal availableQuantity;

    protected TicketTypeEntity() {
    }

    public TicketTypeEntity(UUID id, UUID eventId, String name, BigDecimal price, String currency,
                           BigDecimal quantity, BigDecimal availableQuantity) {
        this.id = id;
        this.eventId = eventId;
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.quantity = quantity;
        this.availableQuantity = availableQuantity;
    }

    // Getters
    public UUID getId() { return id; }
    public UUID getEventId() { return eventId; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public String getCurrency() { return currency; }
    public BigDecimal getQuantity() { return quantity; }
    public BigDecimal getAvailableQuantity() { return availableQuantity; }

    // Setters for updates
    public void setAvailableQuantity(BigDecimal availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
}