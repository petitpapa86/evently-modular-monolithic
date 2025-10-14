package com.evently.modules.users.presentation.users;

import com.evently.common.application.IMediator;
import com.evently.common.domain.Result;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.common.presentation.endpoints.Permissions;
import com.evently.common.presentation.endpoints.Tags;
import com.evently.common.presentation.results.ApiResults;
import com.evently.modules.users.application.users.getuser.GetUserQuery;
import com.evently.modules.users.application.users.getuser.UserResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.UUID;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.RequestPredicates.GET;

public class GetUserProfile implements IEndpoint {

    private final IMediator mediator;

    public GetUserProfile(IMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public RouterFunction<ServerResponse> mapEndpoint() {
        return route(GET("/users/profile"), this::handle)
                .withAttribute("tags", new String[]{Tags.Users})
                .withAttribute("permissions", new String[]{Permissions.GetUser});
    }

    private ServerResponse handle(ServerRequest request) {
        try {
            Authentication authentication = (Authentication) request.servletRequest().getUserPrincipal();
            // For now, we'll use a hardcoded user ID. In a real implementation,
            // you'd extract the user ID from the authentication context
            UUID userId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"); // Placeholder

            GetUserQuery query = new GetUserQuery(userId);
            Result<UserResponse> result = mediator.send(query);

            return result.match(
                    response -> ServerResponse.ok().body(response),
                    () -> ApiResults.problem(result)
            );
        } catch (Exception e) {
            return ServerResponse.status(500).body("Internal server error");
        }
    }
}