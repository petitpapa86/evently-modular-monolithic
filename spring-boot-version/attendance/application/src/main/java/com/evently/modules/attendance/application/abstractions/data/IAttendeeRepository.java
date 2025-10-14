package com.evently.modules.attendance.application.abstractions.data;

import com.evently.modules.attendance.domain.attendees.Attendee;

import java.util.UUID;

public interface IAttendeeRepository {
    void insert(Attendee attendee);
    Attendee findAttendeeById(UUID id);
}