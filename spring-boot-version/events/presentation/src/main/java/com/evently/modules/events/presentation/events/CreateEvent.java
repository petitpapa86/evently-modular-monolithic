package com.evently.modules.events.presentation.events;

import com.evently.common.application.IMediator;
import com.evently.common.domain.Result;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.common.presentation.results.ApiResults;
import com.evently.modules.events.application.events.createevent.CreateEventCommand;
import com.evently.modules.events.presentation.Permissions;
import com.evently.modules.events.presentation.Tags;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.RequestPredicates.POST;

public class CreateEvent implements IEndpoint {

    private final IMediator mediator;

    public CreateEvent(IMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public RouterFunction<ServerResponse> mapEndpoint() {
        return route(POST("/events"), this::handle)
                .withAttribute("tags", new String[]{Tags.Events})
                .withAttribute("permissions", new String[]{Permissions.ModifyEvents});
    }

    private ServerResponse handle(ServerRequest request) {
        try {
            CreateEventRequest createRequest = request.body(CreateEventRequest.class);

            CreateEventCommand command = new CreateEventCommand(
                    createRequest.categoryId(),
                    createRequest.title(),
                    createRequest.description(),
                    createRequest.location(),
                    createRequest.startsAtUtc(),
                    createRequest.endsAtUtc()
            );

            Result<UUID> result = mediator.send(command);

            return result.match(
                    eventId -> ServerResponse.ok().body("Event created with ID: " + eventId),
                    () -> ApiResults.problem(result)
            );
        } catch (Exception e) {
            return ServerResponse.status(400).body("Invalid request");
        }
    }

    public record CreateEventRequest(
            UUID categoryId,
            String title,
            String description,
            String location,
            LocalDateTime startsAtUtc,
            Optional<LocalDateTime> endsAtUtc) {
    }
}