package com.evently.modules.events.domain.events;

import com.evently.common.domain.DomainEvent;
import java.util.UUID;

public class EventPublishedDomainEvent extends DomainEvent {
    private final UUID eventId;

    public EventPublishedDomainEvent(UUID eventId) {
        this.eventId = eventId;
    }

    public UUID getEventId() {
        return eventId;
    }
}