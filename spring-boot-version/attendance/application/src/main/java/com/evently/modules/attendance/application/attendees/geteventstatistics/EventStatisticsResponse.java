package com.evently.modules.attendance.application.attendees.geteventstatistics;

import java.util.UUID;

public record EventStatisticsResponse(
        UUID eventId,
        int totalAttendees,
        int checkedInAttendees
) {
}