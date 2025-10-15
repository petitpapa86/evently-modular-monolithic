package com.evently.modules.ticketing.application.customers;

import com.evently.common.application.IIntegrationEventHandler;
import com.evently.common.domain.Result;
import com.evently.modules.ticketing.application.customers.CreateCustomerCommand;
import com.evently.modules.ticketing.application.customers.CreateCustomerCommandHandler;
import com.evently.modules.users.integrationevents.UserRegisteredIntegrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component("ticketingUserRegisteredIntegrationEventHandler")
public class UserRegisteredIntegrationEventHandler implements IIntegrationEventHandler<UserRegisteredIntegrationEvent> {

    private static final Logger logger = LoggerFactory.getLogger(UserRegisteredIntegrationEventHandler.class);

    private final CreateCustomerCommandHandler createCustomerHandler;

    public UserRegisteredIntegrationEventHandler(CreateCustomerCommandHandler createCustomerHandler) {
        this.createCustomerHandler = createCustomerHandler;
    }

    @EventListener
    @Override
    public void handle(UserRegisteredIntegrationEvent event) {
        logger.info("Handling UserRegisteredIntegrationEvent for user: {}", event.getUserId());

        Result<Void> result = createCustomerHandler.handle(new CreateCustomerCommand(
            event.getUserId(),
            event.getEmail(),
            event.getFirstName(),
            event.getLastName()
        ));

        if (result.isFailure()) {
            logger.error("Failed to create customer for user {}: {}", event.getUserId(), result.getErrors());
        } else {
            logger.info("Successfully created customer for user: {}", event.getUserId());
        }
    }

    @Override
    public Class<UserRegisteredIntegrationEvent> getEventType() {
        return UserRegisteredIntegrationEvent.class;
    }
}