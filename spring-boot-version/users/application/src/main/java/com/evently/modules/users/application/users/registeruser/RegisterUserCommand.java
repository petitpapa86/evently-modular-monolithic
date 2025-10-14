package com.evently.modules.users.application.users.registeruser;

import com.evently.common.application.ICommand;
import java.util.UUID;

public record RegisterUserCommand(
    String email,
    String firstName,
    String lastName,
    String identityId) implements ICommand<UUID> {
}