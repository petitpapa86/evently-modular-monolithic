package com.evently.modules.events.domain.events;

import com.evently.common.domain.DomainEvent;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class EventRescheduledDomainEvent extends DomainEvent {
    private final UUID eventId;
    private final LocalDateTime startsAtUtc;
    private final Optional<LocalDateTime> endsAtUtc;

    public EventRescheduledDomainEvent(UUID eventId, LocalDateTime startsAtUtc, Optional<LocalDateTime> endsAtUtc) {
        this.eventId = eventId;
        this.startsAtUtc = startsAtUtc;
        this.endsAtUtc = endsAtUtc;
    }

    public UUID getEventId() {
        return eventId;
    }

    public LocalDateTime getStartsAtUtc() {
        return startsAtUtc;
    }

    public Optional<LocalDateTime> getEndsAtUtc() {
        return endsAtUtc;
    }
}