package com.evently.modules.users.application.users.updateuser;

import com.evently.common.application.ICommand;
import java.util.UUID;

public record UpdateUserCommand(UUID userId, String firstName, String lastName) implements ICommand<Void> {
}