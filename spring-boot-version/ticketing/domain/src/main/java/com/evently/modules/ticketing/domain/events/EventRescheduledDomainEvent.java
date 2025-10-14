package com.evently.modules.ticketing.domain.events;

import com.evently.common.domain.IDomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventRescheduledDomainEvent(UUID eventId, LocalDateTime startsAtUtc, LocalDateTime endsAtUtc)
    implements IDomainEvent {
}