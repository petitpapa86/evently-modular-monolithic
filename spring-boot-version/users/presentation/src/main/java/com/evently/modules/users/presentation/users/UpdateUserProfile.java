package com.evently.modules.users.presentation.users;

import com.evently.common.application.IMediator;
import com.evently.common.domain.Result;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.common.presentation.endpoints.Permissions;
import com.evently.common.presentation.endpoints.Tags;
import com.evently.common.presentation.results.ApiResults;
import com.evently.modules.users.application.users.updateuser.UpdateUserCommand;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.UUID;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.RequestPredicates.PUT;

public class UpdateUserProfile implements IEndpoint {

    private final IMediator mediator;

    public UpdateUserProfile(IMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public RouterFunction<ServerResponse> mapEndpoint() {
        return route(PUT("/users/profile"), this::handle)
                .withAttribute("tags", new String[]{Tags.Users})
                .withAttribute("permissions", new String[]{Permissions.ModifyUser});
    }

    private ServerResponse handle(ServerRequest request) {
        try {
            UpdateUserRequest updateRequest = request.body(UpdateUserRequest.class);
            Authentication authentication = (Authentication) request.servletRequest().getUserPrincipal();

            // For now, we'll use a hardcoded user ID. In a real implementation,
            // you'd extract the user ID from the authentication context
            UUID userId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"); // Placeholder

            UpdateUserCommand command = new UpdateUserCommand(
                    userId,
                    updateRequest.firstName(),
                    updateRequest.lastName()
            );

            Result<Void> result = mediator.send(command);

            return result.match(
                    success -> ServerResponse.ok().build(),
                    () -> ApiResults.problem(result)
            );
        } catch (Exception e) {
            return ServerResponse.status(400).body("Invalid request");
        }
    }

    public record UpdateUserRequest(String firstName, String lastName) {
    }
}