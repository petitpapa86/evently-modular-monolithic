package com.evently.modules.attendance.application.attendees.getattendee;

import com.evently.common.application.messaging.IQuery;
import com.evently.modules.attendance.domain.attendees.Attendee;

import java.util.Optional;
import java.util.UUID;

public record GetAttendeeQuery(UUID attendeeId) implements IQuery<Optional<Attendee>> {
}