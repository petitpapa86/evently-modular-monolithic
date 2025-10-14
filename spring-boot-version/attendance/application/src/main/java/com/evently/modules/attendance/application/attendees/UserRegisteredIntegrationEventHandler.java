package com.evently.modules.attendance.application.attendees;

import com.evently.common.application.IIntegrationEventHandler;
import com.evently.common.domain.Result;
import com.evently.modules.attendance.application.attendees.CreateAttendeeFromUserCommand;
import com.evently.modules.attendance.application.attendees.CreateAttendeeFromUserCommandHandler;
import com.evently.modules.users.integrationevents.UserRegisteredIntegrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserRegisteredIntegrationEventHandler implements IIntegrationEventHandler<UserRegisteredIntegrationEvent> {

    private static final Logger logger = LoggerFactory.getLogger(UserRegisteredIntegrationEventHandler.class);

    private final CreateAttendeeFromUserCommandHandler createAttendeeHandler;

    public UserRegisteredIntegrationEventHandler(CreateAttendeeFromUserCommandHandler createAttendeeHandler) {
        this.createAttendeeHandler = createAttendeeHandler;
    }

    @EventListener
    @Override
    public void handle(UserRegisteredIntegrationEvent event) {
        logger.info("Handling UserRegisteredIntegrationEvent for user: {}", event.getUserId());

        Result<Void> result = createAttendeeHandler.handle(new CreateAttendeeFromUserCommand(
            event.getUserId(),
            event.getEmail(),
            event.getFirstName(),
            event.getLastName()
        ));

        if (result.isFailure()) {
            logger.error("Failed to create attendee for user {}: {}", event.getUserId(), result.getErrors());
        } else {
            logger.info("Successfully created attendee for user: {}", event.getUserId());
        }
    }

    @Override
    public Class<UserRegisteredIntegrationEvent> getEventType() {
        return UserRegisteredIntegrationEvent.class;
    }
}