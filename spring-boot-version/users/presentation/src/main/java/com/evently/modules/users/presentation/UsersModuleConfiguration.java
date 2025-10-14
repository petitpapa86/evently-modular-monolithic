package com.evently.modules.users.presentation;

import com.evently.common.application.Mediator;
import com.evently.common.infrastructure.authentication.UserIdentityService;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.modules.users.application.users.getuser.GetUserQuery;
import com.evently.modules.users.application.users.getuser.GetUserQueryHandler;
import com.evently.modules.users.application.users.getuserpermissions.GetUserPermissionsQuery;
import com.evently.modules.users.application.users.getuserpermissions.GetUserPermissionsQueryHandler;
import com.evently.modules.users.application.users.registeruser.RegisterUserCommand;
import com.evently.modules.users.application.users.registeruser.RegisterUserCommandHandler;
import com.evently.modules.users.application.users.updateuser.UpdateUserCommand;
import com.evently.modules.users.application.users.updateuser.UpdateUserCommandHandler;
import com.evently.modules.users.presentation.users.GetUserPermissions;
import com.evently.modules.users.presentation.users.GetUserProfile;
import com.evently.modules.users.presentation.users.RegisterUser;
import com.evently.modules.users.presentation.users.UpdateUserProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import jakarta.annotation.PostConstruct;

import java.util.List;

@Configuration
@Import({
    RegisterUserCommandHandler.class,
    GetUserQueryHandler.class,
    UpdateUserCommandHandler.class,
    GetUserPermissionsQueryHandler.class
})
public class UsersModuleConfiguration {

    private final Mediator mediator;
    private final RegisterUserCommandHandler registerUserHandler;
    private final GetUserQueryHandler getUserHandler;
    private final UpdateUserCommandHandler updateUserHandler;
    private final GetUserPermissionsQueryHandler getUserPermissionsHandler;
    private final UserIdentityService userIdentityService;

    public UsersModuleConfiguration(Mediator mediator,
                                  RegisterUserCommandHandler registerUserHandler,
                                  GetUserQueryHandler getUserHandler,
                                  UpdateUserCommandHandler updateUserHandler,
                                  GetUserPermissionsQueryHandler getUserPermissionsHandler,
                                  UserIdentityService userIdentityService) {
        this.mediator = mediator;
        this.registerUserHandler = registerUserHandler;
        this.getUserHandler = getUserHandler;
        this.updateUserHandler = updateUserHandler;
        this.getUserPermissionsHandler = getUserPermissionsHandler;
        this.userIdentityService = userIdentityService;
    }

    @PostConstruct
    public void registerHandlers() {
        mediator.registerCommandHandler(RegisterUserCommand.class, registerUserHandler);
        mediator.registerCommandHandler(UpdateUserCommand.class, updateUserHandler);
        mediator.registerQueryHandler(GetUserQuery.class, getUserHandler);
        mediator.registerQueryHandler(GetUserPermissionsQuery.class, getUserPermissionsHandler);
    }

    @Bean
    public RegisterUser registerUserEndpoint() {
        return new RegisterUser(mediator);
    }

    @Bean
    public GetUserProfile getUserProfileEndpoint() {
        return new GetUserProfile(mediator, userIdentityService);
    }

    @Bean
    public GetUserPermissions getUserPermissionsEndpoint() {
        return new GetUserPermissions(mediator, userIdentityService);
    }

    @Bean
    public UpdateUserProfile updateUserProfileEndpoint() {
        return new UpdateUserProfile(mediator, userIdentityService);
    }

    @Bean
    public RouterFunction<ServerResponse> usersEndpoints(List<IEndpoint> endpoints) {
        return endpoints.stream()
                .map(IEndpoint::mapEndpoint)
                .reduce(RouterFunction::and)
                .orElseThrow(() -> new IllegalStateException("No endpoints found"));
    }
}