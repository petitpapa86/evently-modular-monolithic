package com.evently.modules.ticketing.infrastructure.orders;

import com.evently.common.application.IDomainEventHandler;
import com.evently.modules.users.domain.users.UserProfileUpdatedDomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserProfileUpdatedDomainEventHandler implements IDomainEventHandler<UserProfileUpdatedDomainEvent> {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileUpdatedDomainEventHandler.class);

    @Override
    public void handle(UserProfileUpdatedDomainEvent event) {
        logger.info("Handling UserProfileUpdatedDomainEvent for user: {} - {} {}",
                    event.getUserId(), event.getFirstName(), event.getLastName());

        // In a real implementation, this would update the customer information in the ticketing system
        logger.info("User {} profile updated. Ticketing module notified - customer record can be updated.", event.getUserId());
    }

    @Override
    public Class<UserProfileUpdatedDomainEvent> getEventType() {
        return UserProfileUpdatedDomainEvent.class;
    }
}