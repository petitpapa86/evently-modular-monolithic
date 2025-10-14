package com.evently.common.application.authorization;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public record PermissionsResponse(UUID userId, Set<String> permissions) {
}