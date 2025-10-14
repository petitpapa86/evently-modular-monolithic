package com.evently.modules.users.application.users.getuserpermissions;

import com.evently.common.application.authorization.PermissionsResponse;
import com.evently.common.application.messaging.IQueryHandler;
import com.evently.common.domain.Result;
import com.evently.modules.users.domain.users.IUserRepository;
import com.evently.modules.users.domain.users.Permission;
import com.evently.modules.users.domain.users.User;
import com.evently.modules.users.domain.users.UserErrors;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GetUserPermissionsQueryHandler implements IQueryHandler<GetUserPermissionsQuery, PermissionsResponse> {

    private final IUserRepository userRepository;

    public GetUserPermissionsQueryHandler(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Result<PermissionsResponse> handle(GetUserPermissionsQuery request) {
        Optional<User> userOpt = userRepository.getByIdentityId(request.identityId());
        if (userOpt.isEmpty()) {
            return Result.failure(UserErrors.notFound(request.identityId()));
        }

        User user = userOpt.get();

        // Get permissions from the domain model
        Set<String> permissions = user.getPermissions().stream()
            .map(Permission::getCode)
            .collect(java.util.stream.Collectors.toSet());

        return Result.success(new PermissionsResponse(user.getId(), permissions));
    }
}