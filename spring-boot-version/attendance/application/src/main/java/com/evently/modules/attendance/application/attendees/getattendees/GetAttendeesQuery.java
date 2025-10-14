package com.evently.modules.attendance.application.attendees.getattendees;

import com.evently.common.application.messaging.IQuery;
import com.evently.modules.attendance.domain.attendees.Attendee;

import java.util.List;

public record GetAttendeesQuery() implements IQuery<List<Attendee>> {
}