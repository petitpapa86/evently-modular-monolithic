package com.evently.modules.attendance.infrastructure.attendees;

import com.evently.common.application.IDomainEventHandler;
import com.evently.modules.users.domain.users.UserRegisteredDomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserRegisteredDomainEventHandler implements IDomainEventHandler<UserRegisteredDomainEvent> {

    private static final Logger logger = LoggerFactory.getLogger(UserRegisteredDomainEventHandler.class);

    @Override
    public void handle(UserRegisteredDomainEvent event) {
        logger.info("Handling UserRegisteredDomainEvent for user: {}", event.getUserId());

        // In a real implementation, this would create an attendee record or perform
        // other attendance-related initialization for the new user
        // For now, just log the event
        logger.info("User {} has registered. Attendance module notified.", event.getUserId());
    }

    @Override
    public Class<UserRegisteredDomainEvent> getEventType() {
        return UserRegisteredDomainEvent.class;
    }
}