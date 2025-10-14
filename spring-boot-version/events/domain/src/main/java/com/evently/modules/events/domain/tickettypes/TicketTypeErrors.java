package com.evently.modules.events.domain.tickettypes;

import com.evently.common.domain.Error;
import java.util.UUID;

public class TicketTypeErrors {
    public static Error notFound(UUID ticketTypeId) {
        return Error.notFound("TicketTypes.NotFound", "The ticket type with the identifier " + ticketTypeId + " was not found");
    }
}