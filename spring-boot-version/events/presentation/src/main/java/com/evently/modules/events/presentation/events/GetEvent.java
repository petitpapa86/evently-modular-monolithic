package com.evently.modules.events.presentation.events;

import com.evently.common.application.IMediator;
import com.evently.common.domain.Result;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.common.presentation.results.ApiResults;
import com.evently.modules.events.application.events.getevent.GetEventQuery;
import com.evently.modules.events.domain.events.Event;
import com.evently.modules.events.presentation.Permissions;
import com.evently.modules.events.presentation.Tags;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.RequestPredicates.GET;

public class GetEvent implements IEndpoint {

    private final IMediator mediator;

    public GetEvent(IMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public RouterFunction<ServerResponse> mapEndpoint() {
        return route(GET("/events/{id}"), this::handle)
                .withAttribute("tags", new String[]{Tags.Events})
                .withAttribute("permissions", new String[]{Permissions.GetEvents});
    }

    private ServerResponse handle(ServerRequest request) {
        try {
            UUID eventId = UUID.fromString(request.pathVariable("id"));

            GetEventQuery query = new GetEventQuery(eventId);

            Result<Optional<Event>> result = mediator.send(query);

            return result.match(
                    eventOpt -> {
                        if (eventOpt.isEmpty()) {
                            return ServerResponse.notFound().build();
                        }

                        Event event = eventOpt.get();
                        EventResponse response = new EventResponse(
                                event.getId(),
                                event.getCategoryId(),
                                event.getTitle(),
                                event.getDescription(),
                                event.getLocation(),
                                event.getStartsAtUtc(),
                                event.getEndsAtUtc(),
                                event.getStatus()
                        );

                        return ServerResponse.ok().body(response);
                    },
                    () -> ApiResults.problem(result)
            );
        } catch (Exception e) {
            return ServerResponse.status(400).body("Invalid request");
        }
    }

    public record EventResponse(
            UUID id,
            UUID categoryId,
            String title,
            String description,
            String location,
            java.time.LocalDateTime startsAtUtc,
            Optional<java.time.LocalDateTime> endsAtUtc,
            com.evently.modules.events.domain.events.EventStatus status) {
    }
}