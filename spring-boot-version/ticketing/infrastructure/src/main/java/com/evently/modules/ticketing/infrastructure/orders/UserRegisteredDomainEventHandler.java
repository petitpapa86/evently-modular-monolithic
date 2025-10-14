package com.evently.modules.ticketing.infrastructure.orders;

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

        // In a real implementation, this would create a customer record in the ticketing system
        // or perform other customer-related initialization
        logger.info("User {} has registered. Ticketing module notified - customer record can be created.", event.getUserId());
    }

    @Override
    public Class<UserRegisteredDomainEvent> getEventType() {
        return UserRegisteredDomainEvent.class;
    }
}