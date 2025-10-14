package com.evently.modules.attendance.domain.attendees;

import com.evently.common.domain.DomainEvent;

import java.util.UUID;

public class InvalidCheckInAttemptedDomainEvent extends DomainEvent {
    private final UUID attendeeId;
    private final UUID eventId;
    private final UUID ticketId;
    private final String ticketCode;

    public InvalidCheckInAttemptedDomainEvent(UUID attendeeId, UUID eventId, UUID ticketId, String ticketCode) {
        this.attendeeId = attendeeId;
        this.eventId = eventId;
        this.ticketId = ticketId;
        this.ticketCode = ticketCode;
    }

    public UUID getAttendeeId() {
        return attendeeId;
    }

    public UUID getEventId() {
        return eventId;
    }

    public UUID getTicketId() {
        return ticketId;
    }

    public String getTicketCode() {
        return ticketCode;
    }
}