package com.evently.modules.attendance.application.attendees.geteventstatistics;

import com.evently.common.application.messaging.IQuery;

import java.util.UUID;

public record GetEventStatisticsQuery(UUID eventId) implements IQuery<EventStatisticsResponse> {
}