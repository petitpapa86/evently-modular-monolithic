package com.evently.modules.users.application.users.registeruser;

import com.evently.common.application.IDomainEventHandler;
import com.evently.common.application.IIntegrationEventBus;
import com.evently.common.domain.Result;
import com.evently.modules.users.domain.users.UserRegisteredDomainEvent;
import com.evently.modules.users.integrationevents.UserRegisteredIntegrationEvent;
import com.evently.modules.users.domain.users.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserRegisteredDomainEventHandler implements IDomainEventHandler<UserRegisteredDomainEvent> {

    private static final Logger logger = LoggerFactory.getLogger(UserRegisteredDomainEventHandler.class);

    private final IIntegrationEventBus integrationEventBus;
    private final IUserRepository userRepository;

    public UserRegisteredDomainEventHandler(IIntegrationEventBus integrationEventBus, IUserRepository userRepository) {
        this.integrationEventBus = integrationEventBus;
        this.userRepository = userRepository;
    }

    @Override
    public void handle(UserRegisteredDomainEvent event) {
        logger.info("Handling UserRegisteredDomainEvent for user: {}", event.getUserId());

        // Fetch the user details to include in the integration event
        var userOptional = userRepository.get(event.getUserId());
        if (userOptional.isEmpty()) {
            logger.error("User not found for ID: {}", event.getUserId());
            return;
        }

        var user = userOptional.get();
        var integrationEvent = new UserRegisteredIntegrationEvent(
            event.getUserId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName()
        );

        integrationEventBus.publish(integrationEvent);
    }

    @Override
    public Class<UserRegisteredDomainEvent> getEventType() {
        return UserRegisteredDomainEvent.class;
    }
}