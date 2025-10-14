package com.evently.modules.attendance.application.tickets;

import com.evently.common.application.ICommand;

import java.util.UUID;

public record CheckInTicketCommand(UUID ticketId, UUID attendeeId) implements ICommand<Void> {
}