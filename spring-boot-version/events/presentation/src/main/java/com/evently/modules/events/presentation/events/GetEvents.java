package com.evently.modules.events.presentation.events;

import com.evently.common.application.IMediator;
import com.evently.common.domain.Result;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.modules.events.application.events.getevents.GetEventsQuery;
import com.evently.modules.events.domain.events.Event;
import com.evently.modules.events.presentation.Permissions;
import com.evently.modules.events.presentation.Tags;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.RequestPredicates.GET;

public class GetEvents implements IEndpoint {

    private final IMediator mediator;

    public GetEvents(IMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public RouterFunction<ServerResponse> mapEndpoint() {
        return route(GET("/events"), this::handle)
                .withAttribute("tags", new String[]{Tags.Events})
                .withAttribute("permissions", new String[]{Permissions.GetEvents});
    }

    private ServerResponse handle(ServerRequest request) {
        try {
            GetEventsQuery query = new GetEventsQuery();

            Result<List<Event>> result = mediator.send(query);

            return result.match(
                    events -> {
                        List<GetEvent.EventResponse> responses = events.stream()
                                .map(event -> new GetEvent.EventResponse(
                                        event.getId(),
                                        event.getCategoryId(),
                                        event.getTitle(),
                                        event.getDescription(),
                                        event.getLocation(),
                                        event.getStartsAtUtc(),
                                        event.getEndsAtUtc(),
                                        event.getStatus()
                                ))
                                .toList();

                        return ServerResponse.ok().body(responses);
                    },
                    () -> ServerResponse.status(500).body("Failed to retrieve events")
            );
        } catch (Exception e) {
            return ServerResponse.status(400).body("Invalid request");
        }
    }
}