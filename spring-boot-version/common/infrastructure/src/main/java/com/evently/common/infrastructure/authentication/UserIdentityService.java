package com.evently.common.infrastructure.authentication;

import com.evently.common.domain.Error;
import com.evently.common.domain.Result;
import com.evently.modules.users.application.users.getuser.GetUserQuery;
import com.evently.modules.users.application.users.getuser.UserResponse;
import com.evently.modules.users.application.users.getuserpermissions.GetUserPermissionsQuery;
import com.evently.common.application.authorization.PermissionsResponse;
import com.evently.common.application.IMediator;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserIdentityService {

    private final IMediator mediator;

    public UserIdentityService(IMediator mediator) {
        this.mediator = mediator;
    }

    public Result<String> getCurrentUserIdentityId(Authentication authentication) {
        if (authentication == null) {
            return Result.failure(Error.failure("Auth.NoAuthentication", "No authentication found"));
        }

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return Result.success(jwt.getSubject());
        }

        return Result.failure(Error.failure("Auth.InvalidToken", "Unable to extract identity ID from authentication"));
    }

    public Result<UUID> getCurrentUserId(Authentication authentication) {
        Result<String> identityIdResult = getCurrentUserIdentityId(authentication);
        if (identityIdResult.isFailure()) {
            return Result.failure(identityIdResult.getErrors());
        }

        // For now, we'll create a deterministic UUID from the identity ID
        // In a real implementation, you might want to store a mapping or use the identity ID directly
        UUID userId = UUID.nameUUIDFromBytes(identityIdResult.getValue().getBytes());
        return Result.success(userId);
    }

    public Result<UserResponse> getCurrentUser(Authentication authentication) {
        Result<UUID> userIdResult = getCurrentUserId(authentication);
        if (userIdResult.isFailure()) {
            return Result.failure(userIdResult.getErrors());
        }

        GetUserQuery query = new GetUserQuery(userIdResult.getValue());
        return mediator.send(query);
    }

    public Result<PermissionsResponse> getUserPermissions(String identityId) {
        try {
            GetUserPermissionsQuery query = new GetUserPermissionsQuery(identityId);
            return mediator.send(query);
        } catch (Exception e) {
            return Result.failure(Error.failure("Auth.PermissionCheckFailed", "Failed to retrieve user permissions"));
        }
    }
}