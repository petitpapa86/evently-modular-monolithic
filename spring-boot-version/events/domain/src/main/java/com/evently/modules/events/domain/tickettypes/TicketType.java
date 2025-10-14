package com.evently.modules.events.domain.tickettypes;

import com.evently.common.domain.Entity;
import com.evently.modules.events.domain.events.Event;
import java.math.BigDecimal;
import java.util.UUID;

public class TicketType extends Entity {
    public TicketType() {
    }

    private UUID id;
    private UUID eventId;
    private String name;
    private BigDecimal price;
    private String currency;
    private BigDecimal quantity;

    public static TicketType create(Event event, String name, BigDecimal price, String currency, BigDecimal quantity) {
        TicketType ticketType = new TicketType();
        ticketType.id = UUID.randomUUID();
        ticketType.eventId = event.getId();
        ticketType.name = name;
        ticketType.price = price;
        ticketType.currency = currency;
        ticketType.quantity = quantity;

        return ticketType;
    }

    public void updatePrice(BigDecimal price) {
        if (this.price.compareTo(price) == 0) {
            return;
        }

        this.price = price;

        raise(new TicketTypePriceChangedDomainEvent(id, price));
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public UUID getEventId() {
        return eventId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    // Setters for JPA
    public void setId(UUID id) {
        this.id = id;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}