package com.evently.modules.attendance.domain.attendees;

import com.evently.common.domain.DomainEvent;

import java.util.UUID;

public class AttendeeCheckedInDomainEvent extends DomainEvent {
    private final UUID attendeeId;
    private final UUID eventId;

    public AttendeeCheckedInDomainEvent(UUID attendeeId, UUID eventId) {
        this.attendeeId = attendeeId;
        this.eventId = eventId;
    }

    public UUID getAttendeeId() {
        return attendeeId;
    }

    public UUID getEventId() {
        return eventId;
    }
}