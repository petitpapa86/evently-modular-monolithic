package com.evently.modules.attendance.infrastructure.tickets;

import com.evently.modules.attendance.domain.tickets.Ticket;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tickets")
public class TicketEntity {

    @Id
    private UUID id;
    private String code;
    private UUID attendeeId;
    private UUID eventId;
    private Instant usedAtUtc;

    protected TicketEntity() {
    }

    public TicketEntity(UUID id, String code, UUID attendeeId, UUID eventId) {
        this.id = id;
        this.code = code;
        this.attendeeId = attendeeId;
        this.eventId = eventId;
    }

    public static TicketEntity fromDomain(Ticket ticket) {
        TicketEntity entity = new TicketEntity(
                ticket.getId(),
                ticket.getCode(),
                ticket.getAttendeeId(),
                ticket.getEventId()
        );
        entity.usedAtUtc = ticket.getUsedAtUtc();
        return entity;
    }

    public Ticket toDomain() {
        Ticket ticket = new Ticket(id, code, attendeeId, eventId);
        // Note: usedAtUtc is not set in the domain constructor, but we can add a setter if needed
        return ticket;
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

    public void setUsedAtUtc(Instant usedAtUtc) {
        this.usedAtUtc = usedAtUtc;
    }
}