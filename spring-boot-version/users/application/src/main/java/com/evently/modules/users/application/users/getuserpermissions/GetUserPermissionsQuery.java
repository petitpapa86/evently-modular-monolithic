package com.evently.modules.users.application.users.getuserpermissions;

import com.evently.common.application.authorization.PermissionsResponse;
import com.evently.common.application.messaging.IQuery;

public record GetUserPermissionsQuery(String identityId) implements IQuery<PermissionsResponse> {
}