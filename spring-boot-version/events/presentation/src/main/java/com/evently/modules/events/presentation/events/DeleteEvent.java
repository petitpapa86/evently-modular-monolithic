package com.evently.modules.events.presentation.events;

import com.evently.common.application.IMediator;
import com.evently.common.domain.Result;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.common.presentation.results.ApiResults;
import com.evently.modules.events.application.events.deleteevent.DeleteEventCommand;
import com.evently.modules.events.presentation.Permissions;
import com.evently.modules.events.presentation.Tags;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.UUID;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.RequestPredicates.DELETE;

public class DeleteEvent implements IEndpoint {

    private final IMediator mediator;

    public DeleteEvent(IMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public RouterFunction<ServerResponse> mapEndpoint() {
        return route(DELETE("/events/{id}"), this::handle)
                .withAttribute("tags", new String[]{Tags.Events})
                .withAttribute("permissions", new String[]{Permissions.ModifyEvents});
    }

    private ServerResponse handle(ServerRequest request) {
        try {
            UUID eventId = UUID.fromString(request.pathVariable("id"));

            DeleteEventCommand command = new DeleteEventCommand(eventId);

            Result<Void> result = mediator.send(command);

            return result.match(
                    success -> ServerResponse.ok().body("Event deleted successfully"),
                    () -> ApiResults.problem(result)
            );
        } catch (Exception e) {
            return ServerResponse.status(400).body("Invalid request");
        }
    }
}