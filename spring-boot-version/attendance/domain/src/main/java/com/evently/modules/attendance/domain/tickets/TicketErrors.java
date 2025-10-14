package com.evently.modules.attendance.domain.tickets;

import com.evently.common.domain.Error;

public class TicketErrors {
    public static Error invalidCheckIn() {
        return Error.problem("Ticket.InvalidCheckIn", "The ticket is not valid for check-in.");
    }

    public static Error duplicateCheckIn() {
        return Error.problem("Ticket.DuplicateCheckIn", "The ticket has already been used for check-in.");
    }
}