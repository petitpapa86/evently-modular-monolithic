package com.evently.modules.users.application.users.getuser;

import com.evently.common.application.messaging.IQuery;
import java.util.UUID;

public record GetUserQuery(UUID userId) implements IQuery<UserResponse> {
}