package com.evently.modules.attendance.domain.attendees;

import com.evently.common.domain.Entity;
import com.evently.common.domain.Result;
import com.evently.modules.attendance.domain.tickets.Ticket;
import com.evently.modules.attendance.domain.tickets.TicketErrors;

import java.util.UUID;

public class Attendee extends Entity {
    private Attendee() {
    }

    private UUID id;
    private String email;
    private String firstName;
    private String lastName;

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public static Attendee create(UUID id, String email, String firstName, String lastName) {
        Attendee attendee = new Attendee();
        attendee.id = id;
        attendee.email = email;
        attendee.firstName = firstName;
        attendee.lastName = lastName;
        return attendee;
    }

    public void update(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Result checkIn(Ticket ticket) {
        if (!id.equals(ticket.getAttendeeId())) {
            raise(new InvalidCheckInAttemptedDomainEvent(id, ticket.getEventId(), ticket.getId(), ticket.getCode()));
            return Result.failure(TicketErrors.invalidCheckIn());
        }

        if (ticket.getUsedAtUtc() != null) {
            raise(new DuplicateCheckInAttemptedDomainEvent(id, ticket.getEventId(), ticket.getId(), ticket.getCode()));
            return Result.failure(TicketErrors.duplicateCheckIn());
        }

        ticket.markAsUsed();
        raise(new AttendeeCheckedInDomainEvent(id, ticket.getEventId()));

        return Result.success();
    }
}