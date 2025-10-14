package com.evently.modules.attendance.presentation.attendance;

import com.evently.common.application.IMediator;
import com.evently.common.domain.Result;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.common.presentation.results.ApiResults;
import com.evently.modules.attendance.application.attendees.CreateAttendeeCommand;
import com.evently.modules.attendance.presentation.Tags;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.UUID;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.RequestPredicates.POST;

public class CreateAttendee implements IEndpoint {

    private final IMediator mediator;

    public CreateAttendee(IMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public RouterFunction<ServerResponse> mapEndpoint() {
        return route(POST("/attendees"), this::handle)
                .withAttribute("tags", new String[]{Tags.Attendees});
    }

    private ServerResponse handle(ServerRequest request) {
        try {
            CreateAttendeeRequest createRequest = request.body(CreateAttendeeRequest.class);

            CreateAttendeeCommand command = new CreateAttendeeCommand(
                    UUID.randomUUID(),
                    createRequest.email(),
                    createRequest.firstName(),
                    createRequest.lastName()
            );

            Result<Void> result = mediator.send(command);

            return result.match(
                    success -> ServerResponse.ok().body("Attendee created"),
                    () -> ApiResults.problem(result)
            );
        } catch (Exception e) {
            return ServerResponse.status(400).body("Invalid request");
        }
    }

    public record CreateAttendeeRequest(String email, String firstName, String lastName) {
    }
}