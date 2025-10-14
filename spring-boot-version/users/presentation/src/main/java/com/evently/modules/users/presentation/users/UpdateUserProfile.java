package com.evently.modules.users.presentation.users;

import com.evently.common.application.IMediator;
import com.evently.common.domain.Result;
import com.evently.common.infrastructure.authentication.UserIdentityService;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.common.presentation.endpoints.Permissions;
import com.evently.common.presentation.endpoints.Tags;
import com.evently.common.presentation.results.ApiResults;
import com.evently.modules.users.application.users.updateuser.UpdateUserCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.UUID;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.RequestPredicates.PUT;

public class UpdateUserProfile implements IEndpoint {

    private final IMediator mediator;
    private final UserIdentityService userIdentityService;

    public UpdateUserProfile(IMediator mediator, UserIdentityService userIdentityService) {
        this.mediator = mediator;
        this.userIdentityService = userIdentityService;
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

            Result<UUID> userIdResult = userIdentityService.getCurrentUserId(authentication);
            if (userIdResult.isFailure()) {
                return ApiResults.problem(userIdResult);
            }

            UpdateUserCommand command = new UpdateUserCommand(
                    userIdResult.getValue(),
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

    public record UpdateUserRequest(@NotBlank String firstName, @NotBlank String lastName) {
    }
}