package com.evently.modules.ticketing.domain.events;

import com.evently.common.domain.Entity;
import com.evently.common.domain.IDomainEvent;
import com.evently.common.domain.Result;

import java.math.BigDecimal;
import java.util.UUID;

public class TicketType extends Entity {

    private TicketType() {
    }

    private UUID id;
    private UUID eventId;
    private String name;
    private BigDecimal price;
    private String currency;
    private BigDecimal quantity;
    private BigDecimal availableQuantity;

    public static TicketType create(UUID id, UUID eventId, String name, BigDecimal price,
                                   String currency, BigDecimal quantity) {
        TicketType ticketType = new TicketType();
        ticketType.id = id;
        ticketType.eventId = eventId;
        ticketType.name = name;
        ticketType.price = price;
        ticketType.currency = currency;
        ticketType.quantity = quantity;
        ticketType.availableQuantity = quantity;
        return ticketType;
    }

    public void updatePrice(BigDecimal price) {
        this.price = price;
    }

    public Result<Void> reserveQuantity(BigDecimal quantity) {
        if (availableQuantity.compareTo(quantity) < 0) {
            return Result.failure(TicketTypeErrors.notEnoughQuantity(availableQuantity));
        }

        return Result.success();
    }

    public Result updateQuantity(BigDecimal quantity) {
        if (availableQuantity.compareTo(quantity) < 0) {
            return Result.failure(TicketTypeErrors.notEnoughQuantity(availableQuantity));
        }

        availableQuantity = availableQuantity.subtract(quantity);

        if (availableQuantity.compareTo(BigDecimal.ZERO) == 0) {
            raise(new TicketTypeSoldOutDomainEvent(id));
        }

        return Result.success();
    }

    // Getters
    public UUID getId() { return id; }
    public UUID getEventId() { return eventId; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public String getCurrency() { return currency; }
    public BigDecimal getQuantity() { return quantity; }
    public BigDecimal getAvailableQuantity() { return availableQuantity; }
}