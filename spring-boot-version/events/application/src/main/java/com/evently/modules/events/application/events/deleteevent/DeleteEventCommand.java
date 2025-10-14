package com.evently.modules.events.application.events.deleteevent;

import com.evently.common.application.ICommand;
import com.evently.common.application.IDateTimeProvider;
import java.util.UUID;

public record DeleteEventCommand(
    UUID eventId) implements ICommand<Void> {
}