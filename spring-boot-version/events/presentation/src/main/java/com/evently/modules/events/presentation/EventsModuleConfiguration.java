package com.evently.modules.events.presentation;

import com.evently.common.application.Mediator;
import com.evently.modules.events.application.events.createevent.CreateEventCommand;
import com.evently.modules.events.application.events.createevent.CreateEventCommandHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import jakarta.annotation.PostConstruct;

@Configuration
@Import(CreateEventCommandHandler.class)
public class EventsModuleConfiguration {

    private final Mediator mediator;
    private final CreateEventCommandHandler createEventHandler;

    public EventsModuleConfiguration(Mediator mediator, CreateEventCommandHandler createEventHandler) {
        this.mediator = mediator;
        this.createEventHandler = createEventHandler;
    }

    @PostConstruct
    public void registerHandlers() {
        mediator.registerCommandHandler(CreateEventCommand.class, createEventHandler);
    }
}