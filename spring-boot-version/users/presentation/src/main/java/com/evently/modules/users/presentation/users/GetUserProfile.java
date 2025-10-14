package com.evently.modules.users.presentation.users;

import com.evently.common.infrastructure.authentication.UserIdentityService;
import com.evently.common.domain.Result;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.common.presentation.endpoints.Permissions;
import com.evently.common.presentation.endpoints.Tags;
import com.evently.common.presentation.results.ApiResults;
import com.evently.modules.users.application.users.getuser.GetUserQuery;
import com.evently.modules.users.application.users.getuser.UserResponse;
import com.evently.common.application.IMediator;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.UUID;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.RequestPredicates.GET;

public class GetUserProfile implements IEndpoint {

    private final IMediator mediator;
    private final UserIdentityService userIdentityService;

    public GetUserProfile(IMediator mediator, UserIdentityService userIdentityService) {
        this.mediator = mediator;
        this.userIdentityService = userIdentityService;
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

            Result<UUID> userIdResult = userIdentityService.getCurrentUserId(authentication);
            if (userIdResult.isFailure()) {
                return ApiResults.problem(userIdResult);
            }

            GetUserQuery query = new GetUserQuery(userIdResult.getValue());
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