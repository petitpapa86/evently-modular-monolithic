package com.evently.modules.users.presentation.users;

import com.evently.common.application.IMediator;
import com.evently.common.application.authorization.PermissionsResponse;
import com.evently.common.domain.Result;
import com.evently.common.infrastructure.authentication.UserIdentityService;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.common.presentation.endpoints.Permissions;
import com.evently.common.presentation.endpoints.Tags;
import com.evently.common.presentation.results.ApiResults;
import com.evently.modules.users.application.users.getuserpermissions.GetUserPermissionsQuery;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.RequestPredicates.GET;

public class GetUserPermissions implements IEndpoint {

    private final IMediator mediator;
    private final UserIdentityService userIdentityService;

    public GetUserPermissions(IMediator mediator, UserIdentityService userIdentityService) {
        this.mediator = mediator;
        this.userIdentityService = userIdentityService;
    }

    @Override
    public RouterFunction<ServerResponse> mapEndpoint() {
        return route(GET("/users/permissions"), this::handle)
                .withAttribute("tags", new String[]{Tags.Users})
                .withAttribute("permissions", new String[]{Permissions.GetUser});
    }

    private ServerResponse handle(ServerRequest request) {
        try {
            Authentication authentication = (Authentication) request.servletRequest().getUserPrincipal();

            Result<String> identityIdResult = userIdentityService.getCurrentUserIdentityId(authentication);
            if (identityIdResult.isFailure()) {
                return ApiResults.problem(identityIdResult);
            }

            GetUserPermissionsQuery query = new GetUserPermissionsQuery(identityIdResult.getValue());
            Result<PermissionsResponse> result = mediator.send(query);

            return result.match(
                    response -> ServerResponse.ok().body(response),
                    () -> ApiResults.problem(result)
            );
        } catch (Exception e) {
            return ServerResponse.status(500).body("Internal server error");
        }
    }
}