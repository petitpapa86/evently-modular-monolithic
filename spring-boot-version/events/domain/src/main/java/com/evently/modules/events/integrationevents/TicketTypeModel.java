package com.evently.modules.events.integrationevents;

import java.math.BigDecimal;
import java.util.UUID;

public class TicketTypeModel {
    private UUID id;
    private UUID eventId;
    private String name;
    private BigDecimal price;
    private String currency;
    private BigDecimal quantity;

    public TicketTypeModel() {
    }

    public TicketTypeModel(UUID id, UUID eventId, String name, BigDecimal price, String currency, BigDecimal quantity) {
        this.id = id;
        this.eventId = eventId;
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.quantity = quantity;
    }

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getEventId() { return eventId; }
    public void setEventId(UUID eventId) { this.eventId = eventId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
}