package com.evently.modules.ticketing.domain.events;

import com.evently.common.domain.Entity;
import com.evently.common.domain.IDomainEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Event extends Entity {

    private Event() {
    }

    private UUID id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime startsAtUtc;
    private LocalDateTime endsAtUtc;
    private boolean canceled;

    public static Event create(UUID id, String title, String description, String location,
                              LocalDateTime startsAtUtc, LocalDateTime endsAtUtc) {
        Event event = new Event();
        event.id = id;
        event.title = title;
        event.description = description;
        event.location = location;
        event.startsAtUtc = startsAtUtc;
        event.endsAtUtc = endsAtUtc;
        event.canceled = false;
        return event;
    }

    public void reschedule(LocalDateTime startsAtUtc, LocalDateTime endsAtUtc) {
        this.startsAtUtc = startsAtUtc;
        this.endsAtUtc = endsAtUtc;
        raise(new EventRescheduledDomainEvent(id, startsAtUtc, endsAtUtc));
    }

    public void cancel() {
        if (canceled) {
            return;
        }
        canceled = true;
        raise(new EventCanceledDomainEvent(id));
    }

    public void paymentsRefunded() {
        raise(new EventPaymentsRefundedDomainEvent(id));
    }

    public void ticketsArchived() {
        raise(new EventTicketsArchivedDomainEvent(id));
    }

    // Getters
    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public LocalDateTime getStartsAtUtc() { return startsAtUtc; }
    public LocalDateTime getEndsAtUtc() { return endsAtUtc; }
    public boolean isCanceled() { return canceled; }
}