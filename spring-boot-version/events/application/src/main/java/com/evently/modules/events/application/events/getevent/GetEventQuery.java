package com.evently.modules.events.application.events.getevent;

import com.evently.common.application.messaging.IQuery;
import com.evently.modules.events.domain.events.Event;
import java.util.Optional;

public record GetEventQuery(
    java.util.UUID eventId) implements IQuery<Optional<Event>> {
}