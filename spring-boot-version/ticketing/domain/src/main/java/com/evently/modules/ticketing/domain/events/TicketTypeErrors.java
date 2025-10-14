package com.evently.modules.ticketing.domain.events;

import com.evently.common.domain.Error;

import java.math.BigDecimal;
import java.util.UUID;

public class TicketTypeErrors {

    public static Error notFound(UUID ticketTypeId) {
        return Error.notFound(
            "TicketType.NotFound",
            "The ticket type with the identifier " + ticketTypeId + " was not found"
        );
    }

    public static Error notEnoughQuantity(BigDecimal availableQuantity) {
        return Error.failure(
            "TicketType.NotEnoughQuantity",
            "The ticket type does not have enough quantity. Available quantity: " + availableQuantity
        );
    }
}