package com.evently.modules.users.presentation.users;

import com.evently.common.application.IMediator;
import com.evently.common.domain.Result;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.common.presentation.endpoints.Tags;
import com.evently.common.presentation.results.ApiResults;
import com.evently.modules.users.application.users.registeruser.RegisterUserCommand;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.UUID;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.RequestPredicates.POST;

public class RegisterUser implements IEndpoint {

    private final IMediator mediator;

    public RegisterUser(IMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public RouterFunction<ServerResponse> mapEndpoint() {
        return route(POST("/users/register"), this::handle)
                .withAttribute("tags", new String[]{Tags.Users});
    }

    private ServerResponse handle(ServerRequest request) {
        try {
            RegisterUserRequest registerRequest = request.body(RegisterUserRequest.class);

            RegisterUserCommand command = new RegisterUserCommand(
                    registerRequest.email(),
                    registerRequest.firstName(),
                    registerRequest.lastName(),
                    UUID.randomUUID().toString() // Generate identityId for now
            );

            Result<UUID> result = mediator.send(command);

            return result.match(
                    userId -> ServerResponse.ok().body(userId),
                    () -> ApiResults.problem(result)
            );
        } catch (Exception e) {
            return ServerResponse.status(400).body("Invalid request");
        }
    }

    public record RegisterUserRequest(
            String email,
            String firstName,
            String lastName) {
    }
}