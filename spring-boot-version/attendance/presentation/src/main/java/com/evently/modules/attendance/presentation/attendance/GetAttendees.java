package com.evently.modules.attendance.presentation.attendance;

import com.evently.common.application.IMediator;
import com.evently.common.domain.Result;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.modules.attendance.application.attendees.getattendees.GetAttendeesQuery;
import com.evently.modules.attendance.domain.attendees.Attendee;
import com.evently.modules.attendance.presentation.Permissions;
import com.evently.modules.attendance.presentation.Tags;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.List;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.RequestPredicates.GET;

public class GetAttendees implements IEndpoint {

    private final IMediator mediator;

    public GetAttendees(IMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public RouterFunction<ServerResponse> mapEndpoint() {
        return route(GET("/attendees"), this::handle)
                .withAttribute("tags", new String[]{Tags.Attendees})
                .withAttribute("permissions", new String[]{Permissions.GetAttendees});
    }

    private ServerResponse handle(ServerRequest request) {
        try {
            GetAttendeesQuery query = new GetAttendeesQuery();

            Result<List<Attendee>> result = mediator.send(query);

            return result.match(
                    attendees -> {
                        List<GetAttendee.AttendeeResponse> responses = attendees.stream()
                                .map(attendee -> new GetAttendee.AttendeeResponse(
                                        attendee.getId(),
                                        attendee.getEmail(),
                                        attendee.getFirstName(),
                                        attendee.getLastName()
                                ))
                                .toList();

                        return ServerResponse.ok().body(responses);
                    },
                    () -> ServerResponse.status(500).body("Failed to retrieve attendees")
            );
        } catch (Exception e) {
            return ServerResponse.status(500).body("Internal server error");
        }
    }
}