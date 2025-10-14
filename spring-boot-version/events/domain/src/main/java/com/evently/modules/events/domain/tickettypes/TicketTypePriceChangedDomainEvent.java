package com.evently.modules.events.domain.tickettypes;

import com.evently.common.domain.DomainEvent;
import java.math.BigDecimal;
import java.util.UUID;

public class TicketTypePriceChangedDomainEvent extends DomainEvent {
    private final UUID ticketTypeId;
    private final BigDecimal price;

    public TicketTypePriceChangedDomainEvent(UUID ticketTypeId, BigDecimal price) {
        this.ticketTypeId = ticketTypeId;
        this.price = price;
    }

    public UUID getTicketTypeId() {
        return ticketTypeId;
    }

    public BigDecimal getPrice() {
        return price;
    }
}