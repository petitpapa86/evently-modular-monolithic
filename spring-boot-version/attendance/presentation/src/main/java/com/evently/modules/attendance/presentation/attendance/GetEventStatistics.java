package com.evently.modules.attendance.presentation.attendance;

import com.evently.common.application.IMediator;
import com.evently.common.domain.Result;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.modules.attendance.application.attendees.geteventstatistics.GetEventStatisticsQuery;
import com.evently.modules.attendance.application.attendees.geteventstatistics.EventStatisticsResponse;
import com.evently.modules.attendance.presentation.Permissions;
import com.evently.modules.attendance.presentation.Tags;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.UUID;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.RequestPredicates.GET;

public class GetEventStatistics implements IEndpoint {

    private final IMediator mediator;

    public GetEventStatistics(IMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public RouterFunction<ServerResponse> mapEndpoint() {
        return route(GET("/events/{eventId}/statistics"), this::handle)
                .withAttribute("tags", new String[]{Tags.EventStatistics})
                .withAttribute("permissions", new String[]{Permissions.GetEventStatistics});
    }

    private ServerResponse handle(ServerRequest request) {
        try {
            UUID eventId = UUID.fromString(request.pathVariable("eventId"));
            GetEventStatisticsQuery query = new GetEventStatisticsQuery(eventId);

            Result<EventStatisticsResponse> result = mediator.send(query);

            return result.match(
                    response -> ServerResponse.ok().body(response),
                    () -> ServerResponse.status(500).body("Internal server error")
            );
        } catch (IllegalArgumentException e) {
            return ServerResponse.status(400).body("Invalid event ID format");
        } catch (Exception e) {
            return ServerResponse.status(500).body("Internal server error");
        }
    }
}