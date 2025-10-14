package com.evently.modules.attendance.presentation.attendance;

import com.evently.common.application.IMediator;
import com.evently.common.domain.Result;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.modules.attendance.application.attendees.getattendee.GetAttendeeQuery;
import com.evently.modules.attendance.domain.attendees.Attendee;
import com.evently.modules.attendance.presentation.Permissions;
import com.evently.modules.attendance.presentation.Tags;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.RequestPredicates.GET;

public class GetAttendee implements IEndpoint {

    private final IMediator mediator;

    public GetAttendee(IMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public RouterFunction<ServerResponse> mapEndpoint() {
        return route(GET("/attendees/{id}"), this::handle)
                .withAttribute("tags", new String[]{Tags.Attendees})
                .withAttribute("permissions", new String[]{Permissions.GetAttendee});
    }

    private ServerResponse handle(ServerRequest request) {
        try {
            UUID attendeeId = UUID.fromString(request.pathVariable("id"));
            GetAttendeeQuery query = new GetAttendeeQuery(attendeeId);

            Result<Optional<Attendee>> result = mediator.send(query);

            return result.match(
                    attendeeOpt -> {
                        if (attendeeOpt.isEmpty()) {
                            return ServerResponse.notFound().build();
                        }

                        Attendee attendee = attendeeOpt.get();
                        AttendeeResponse response = new AttendeeResponse(
                                attendee.getId(),
                                attendee.getEmail(),
                                attendee.getFirstName(),
                                attendee.getLastName()
                        );
                        return ServerResponse.ok().body(response);
                    },
                    () -> ServerResponse.status(500).body("Internal server error")
            );
        } catch (IllegalArgumentException e) {
            return ServerResponse.status(400).body("Invalid attendee ID format");
        } catch (Exception e) {
            return ServerResponse.status(500).body("Internal server error");
        }
    }

    public record AttendeeResponse(UUID id, String email, String firstName, String lastName) {
    }
}