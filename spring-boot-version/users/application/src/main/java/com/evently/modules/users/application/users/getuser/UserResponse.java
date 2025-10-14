package com.evently.modules.users.application.users.getuser;

import java.util.UUID;

public record UserResponse(UUID id, String email, String firstName, String lastName) {
}