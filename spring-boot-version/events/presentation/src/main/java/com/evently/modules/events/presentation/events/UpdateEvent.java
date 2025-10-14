package com.evently.modules.events.presentation.events;

import com.evently.common.application.IMediator;
import com.evently.common.domain.Result;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.common.presentation.results.ApiResults;
import com.evently.modules.events.application.events.updateevent.UpdateEventCommand;
import com.evently.modules.events.presentation.Permissions;
import com.evently.modules.events.presentation.Tags;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.RequestPredicates.PUT;

public class UpdateEvent implements IEndpoint {

    private final IMediator mediator;

    public UpdateEvent(IMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public RouterFunction<ServerResponse> mapEndpoint() {
        return route(PUT("/events/{id}"), this::handle)
                .withAttribute("tags", new String[]{Tags.Events})
                .withAttribute("permissions", new String[]{Permissions.ModifyEvents});
    }

    private ServerResponse handle(ServerRequest request) {
        try {
            UUID eventId = UUID.fromString(request.pathVariable("id"));

            UpdateEventRequest updateRequest = request.body(UpdateEventRequest.class);

            UpdateEventCommand command = new UpdateEventCommand(
                    eventId,
                    updateRequest.title(),
                    updateRequest.description(),
                    updateRequest.location(),
                    updateRequest.startsAtUtc(),
                    updateRequest.endsAtUtc()
            );

            Result<Void> result = mediator.send(command);

            return result.match(
                    success -> ServerResponse.ok().body("Event updated successfully"),
                    () -> ApiResults.problem(result)
            );
        } catch (Exception e) {
            return ServerResponse.status(400).body("Invalid request");
        }
    }

    public record UpdateEventRequest(
            @NotBlank String title,
            @NotBlank String description,
            @NotBlank String location,
            @NotNull LocalDateTime startsAtUtc,
            Optional<LocalDateTime> endsAtUtc) {
    }
}