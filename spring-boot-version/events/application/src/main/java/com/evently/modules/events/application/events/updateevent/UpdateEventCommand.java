package com.evently.modules.events.application.events.updateevent;

import com.evently.common.application.ICommand;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public record UpdateEventCommand(
    UUID eventId,
    String title,
    String description,
    String location,
    LocalDateTime startsAtUtc,
    Optional<LocalDateTime> endsAtUtc) implements ICommand<Void> {
}