package com.evently.modules.events.presentation;

import com.evently.common.application.Mediator;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.modules.events.application.events.createevent.CreateEventCommand;
import com.evently.modules.events.application.events.createevent.CreateEventCommandHandler;
import com.evently.modules.events.application.events.deleteevent.DeleteEventCommand;
import com.evently.modules.events.application.events.deleteevent.DeleteEventCommandHandler;
import com.evently.modules.events.application.events.getevent.GetEventQuery;
import com.evently.modules.events.application.events.getevent.GetEventQueryHandler;
import com.evently.modules.events.application.events.getevents.GetEventsQuery;
import com.evently.modules.events.application.events.getevents.GetEventsQueryHandler;
import com.evently.modules.events.application.events.updateevent.UpdateEventCommand;
import com.evently.modules.events.application.events.updateevent.UpdateEventCommandHandler;
import com.evently.modules.events.presentation.events.CreateEvent;
import com.evently.modules.events.presentation.events.DeleteEvent;
import com.evently.modules.events.presentation.events.GetEvent;
import com.evently.modules.events.presentation.events.GetEvents;
import com.evently.modules.events.presentation.events.UpdateEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import jakarta.annotation.PostConstruct;

import java.util.List;

@Configuration
@Import({
    CreateEventCommandHandler.class,
    GetEventQueryHandler.class,
    GetEventsQueryHandler.class,
    UpdateEventCommandHandler.class,
    DeleteEventCommandHandler.class
})
public class EventsModuleConfiguration {

    private final Mediator mediator;
    private final CreateEventCommandHandler createEventHandler;
    private final GetEventQueryHandler getEventHandler;
    private final GetEventsQueryHandler getEventsHandler;
    private final UpdateEventCommandHandler updateEventHandler;
    private final DeleteEventCommandHandler deleteEventHandler;

    public EventsModuleConfiguration(Mediator mediator,
                                      CreateEventCommandHandler createEventHandler,
                                      GetEventQueryHandler getEventHandler,
                                      GetEventsQueryHandler getEventsHandler,
                                      UpdateEventCommandHandler updateEventHandler,
                                      DeleteEventCommandHandler deleteEventHandler) {
        this.mediator = mediator;
        this.createEventHandler = createEventHandler;
        this.getEventHandler = getEventHandler;
        this.getEventsHandler = getEventsHandler;
        this.updateEventHandler = updateEventHandler;
        this.deleteEventHandler = deleteEventHandler;
    }

    @PostConstruct
    public void registerHandlers() {
        mediator.registerCommandHandler(CreateEventCommand.class, createEventHandler);
        mediator.registerQueryHandler(GetEventQuery.class, getEventHandler);
        mediator.registerQueryHandler(GetEventsQuery.class, getEventsHandler);
        mediator.registerCommandHandler(UpdateEventCommand.class, updateEventHandler);
        mediator.registerCommandHandler(DeleteEventCommand.class, deleteEventHandler);
    }

    @Bean
    public CreateEvent createEventEndpoint() {
        return new CreateEvent(mediator);
    }

    @Bean
    public GetEvent getEventEndpoint() {
        return new GetEvent(mediator);
    }

    @Bean
    public GetEvents getEventsEndpoint() {
        return new GetEvents(mediator);
    }

    @Bean
    public UpdateEvent updateEventEndpoint() {
        return new UpdateEvent(mediator);
    }

    @Bean
    public DeleteEvent deleteEventEndpoint() {
        return new DeleteEvent(mediator);
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