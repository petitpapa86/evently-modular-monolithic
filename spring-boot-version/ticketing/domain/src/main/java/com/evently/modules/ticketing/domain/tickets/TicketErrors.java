package com.evently.modules.ticketing.domain.tickets;

import com.evently.common.domain.Error;

import java.util.UUID;

public class TicketErrors {

    public static Error notFound(UUID ticketId) {
        return Error.notFound(
            "Ticket.NotFound",
            "The ticket with the identifier " + ticketId + " was not found"
        );
    }
}