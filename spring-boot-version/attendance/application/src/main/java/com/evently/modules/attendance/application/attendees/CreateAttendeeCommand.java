package com.evently.modules.attendance.application.attendees;

import com.evently.common.application.ICommand;

import java.util.UUID;

public record CreateAttendeeCommand(UUID attendeeId, String email, String firstName, String lastName) implements ICommand<Void> {
}