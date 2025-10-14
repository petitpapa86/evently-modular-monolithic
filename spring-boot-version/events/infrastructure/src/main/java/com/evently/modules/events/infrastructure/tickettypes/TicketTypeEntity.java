package com.evently.modules.events.infrastructure.tickettypes;

import com.evently.modules.events.domain.tickettypes.TicketType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity(name = "EventsTicketTypeEntity")
@Table(name = "ticket_types")
public class TicketTypeEntity {
    @Id
    private UUID id;
    private UUID eventId;
    private String name;
    private BigDecimal price;
    private String currency;
    private BigDecimal quantity;

    public TicketTypeEntity() {
    }

    public TicketTypeEntity(UUID id, UUID eventId, String name, BigDecimal price, String currency, BigDecimal quantity) {
        this.id = id;
        this.eventId = eventId;
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.quantity = quantity;
    }

    public TicketType toDomain() {
        TicketType ticketType = new TicketType();
        ticketType.setId(id);
        ticketType.setEventId(eventId);
        ticketType.setName(name);
        ticketType.setPrice(price);
        ticketType.setCurrency(currency);
        ticketType.setQuantity(quantity);
        return ticketType;
    }

    public static TicketTypeEntity fromDomain(TicketType ticketType) {
        return new TicketTypeEntity(ticketType.getId(), ticketType.getEventId(), ticketType.getName(), ticketType.getPrice(), ticketType.getCurrency(), ticketType.getQuantity());
    }

    // getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}