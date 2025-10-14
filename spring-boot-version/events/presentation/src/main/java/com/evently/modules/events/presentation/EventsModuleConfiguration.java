package com.evently.modules.events.presentation;

import com.evently.common.application.Mediator;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.modules.events.application.events.createevent.CreateEventCommand;
import com.evently.modules.events.application.events.createevent.CreateEventCommandHandler;
import com.evently.modules.events.presentation.events.CreateEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import jakarta.annotation.PostConstruct;

import java.util.List;

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

    @Bean
    public CreateEvent createEventEndpoint() {
        return new CreateEvent(mediator);
    }

    @Bean
    public RouterFunction<ServerResponse> eventsEndpoints(List<IEndpoint> endpoints) {
        return endpoints.stream()
                .filter(endpoint -> endpoint.getClass().getPackageName().startsWith("com.evently.modules.events"))
                .map(IEndpoint::mapEndpoint)
                .reduce(RouterFunction::and)
                .orElseThrow(() -> new IllegalStateException("No events endpoints found"));
    }
}