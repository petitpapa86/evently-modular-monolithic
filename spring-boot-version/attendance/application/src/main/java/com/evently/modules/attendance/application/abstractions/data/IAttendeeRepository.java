package com.evently.modules.attendance.application.abstractions.data;

import com.evently.modules.attendance.domain.attendees.Attendee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IAttendeeRepository {
    void insert(Attendee attendee);
    Optional<Attendee> findAttendeeById(UUID id);
    List<Attendee> getAll();
}