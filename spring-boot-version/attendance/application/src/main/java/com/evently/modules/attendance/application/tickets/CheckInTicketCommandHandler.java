package com.evently.modules.attendance.application.tickets;

import com.evently.common.application.ICommandHandler;
import com.evently.common.domain.Result;
import com.evently.modules.attendance.application.abstractions.data.IAttendeeRepository;
import com.evently.modules.attendance.domain.attendees.Attendee;
import com.evently.modules.attendance.domain.tickets.Ticket;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class CheckInTicketCommandHandler implements ICommandHandler<CheckInTicketCommand, Void> {

    private final ITicketRepository ticketRepository;
    private final IAttendeeRepository attendeeRepository;

    public CheckInTicketCommandHandler(ITicketRepository ticketRepository, IAttendeeRepository attendeeRepository) {
        this.ticketRepository = ticketRepository;
        this.attendeeRepository = attendeeRepository;
    }

    @Override
    @Transactional
    public Result<Void> handle(CheckInTicketCommand command) {
        // Get the ticket
        var ticketOpt = ticketRepository.findTicketById(command.ticketId());
        if (ticketOpt.isEmpty()) {
            return Result.failure(com.evently.common.domain.Error.failure("Ticket.NotFound", "Ticket not found"));
        }

        // Get the attendee
        var attendeeOpt = attendeeRepository.findAttendeeById(command.attendeeId());
        if (attendeeOpt.isEmpty()) {
            return Result.failure(com.evently.common.domain.Error.failure("Attendee.NotFound", "Attendee not found"));
        }

        Ticket ticket = ticketOpt.get();
        Attendee attendee = attendeeOpt.get();

        // Perform check-in
        Result<Void> checkInResult = attendee.checkIn(ticket);
        if (checkInResult.isFailure()) {
            return checkInResult;
        }

        // Save the updated ticket
        ticketRepository.insert(ticket);

        return Result.success();
    }
}