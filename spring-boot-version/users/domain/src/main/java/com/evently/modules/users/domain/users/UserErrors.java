package com.evently.modules.users.domain.users;

import com.evently.common.domain.Error;
import java.util.UUID;

public class UserErrors {
    public static Error notFound(UUID userId) {
        return Error.notFound("Users.NotFound", "The user with the identifier " + userId + " not found");
    }

    public static Error notFound(String identityId) {
        return Error.notFound("Users.NotFound", "The user with the IDP identifier " + identityId + " not found");
    }

    public static Error emailAlreadyInUse() {
        return Error.conflict("Users.EmailAlreadyInUse", "The specified email is already in use");
    }

    public static Error roleNotFound(String roleName) {
        return Error.notFound("Users.RoleNotFound", "The role '" + roleName + "' was not found");
    }

    public static Error unauthorized() {
        return Error.problem("Users.Unauthorized", "You are not authorized to perform this action");
    }
}