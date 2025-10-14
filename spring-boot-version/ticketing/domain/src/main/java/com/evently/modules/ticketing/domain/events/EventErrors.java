package com.evently.modules.ticketing.domain.events;

import com.evently.common.domain.Error;

import java.util.UUID;

public class EventErrors {

    public static Error notFound(UUID eventId) {
        return Error.notFound(
            "Event.NotFound",
            "The event with the identifier " + eventId + " was not found"
        );
    }
}