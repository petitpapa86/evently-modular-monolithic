package com.evently.modules.events.domain.events;

import com.evently.common.domain.DomainEvent;
import java.util.UUID;

public class EventCanceledDomainEvent extends DomainEvent {
    private final UUID eventId;

    public EventCanceledDomainEvent(UUID eventId) {
        this.eventId = eventId;
    }

    public UUID getEventId() {
        return eventId;
    }
}