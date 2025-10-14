package com.evently.modules.attendance.domain.tickets;

import java.time.Instant;
import java.util.UUID;

public class Ticket {
    private UUID id;
    private String code;
    private UUID attendeeId;
    private UUID eventId;
    private Instant usedAtUtc;

    public Ticket(UUID id, String code, UUID attendeeId, UUID eventId) {
        this.id = id;
        this.code = code;
        this.attendeeId = attendeeId;
        this.eventId = eventId;
    }

    public UUID getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public UUID getAttendeeId() {
        return attendeeId;
    }

    public UUID getEventId() {
        return eventId;
    }

    public Instant getUsedAtUtc() {
        return usedAtUtc;
    }

    public void markAsUsed() {
        this.usedAtUtc = Instant.now();
    }
}