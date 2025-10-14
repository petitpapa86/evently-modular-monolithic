package com.evently.modules.attendance.application.attendees;

import java.util.UUID;

public record CreateAttendeeFromUserCommand(
    UUID userId,
    String email,
    String firstName,
    String lastName
) {
}