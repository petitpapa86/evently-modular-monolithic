package com.evently.modules.events.domain.events;

import com.evently.common.domain.Error;
import java.util.UUID;

public class EventErrors {
    public static Error notFound(UUID eventId) {
        return Error.notFound("Events.NotFound", "The event with the identifier " + eventId + " was not found");
    }

    public static final Error START_DATE_IN_PAST = Error.problem("Events.StartDateInPast", "The event start date is in the past");

    public static final Error END_DATE_PRECEDES_START_DATE = Error.problem("Events.EndDatePrecedesStartDate", "The event end date precedes the start date");

    public static final Error NO_TICKETS_FOUND = Error.problem("Events.NoTicketsFound", "The event does not have any ticket types defined");

    public static final Error NOT_DRAFT = Error.problem("Events.NotDraft", "The event is not in draft status");

    public static final Error ALREADY_CANCELED = Error.problem("Events.AlreadyCanceled", "The event was already canceled");

    public static final Error ALREADY_STARTED = Error.problem("Events.AlreadyStarted", "The event has already started");
}