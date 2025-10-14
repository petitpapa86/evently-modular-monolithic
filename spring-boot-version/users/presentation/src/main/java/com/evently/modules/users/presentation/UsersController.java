package com.evently.modules.users.presentation;

import com.evently.common.application.IMediator;
import com.evently.common.domain.Result;
import com.evently.modules.users.application.users.getuser.GetUserQuery;
import com.evently.modules.users.application.users.getuser.UserResponse;
import com.evently.modules.users.application.users.getuserpermissions.GetUserPermissionsQuery;
import com.evently.modules.users.application.users.registeruser.RegisterUserCommand;
import com.evently.modules.users.application.users.updateuser.UpdateUserCommand;
import com.evently.common.application.authorization.PermissionsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private final IMediator mediator;

    public UsersController(IMediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody RegisterUserRequest request) {
        RegisterUserCommand command = new RegisterUserCommand(
            request.email(),
            request.firstName(),
            request.lastName(),
            request.identityId()
        );

        Result<UUID> result = mediator.send(command);

        if (result.isSuccess()) {
            return ResponseEntity.ok("User registered with ID: " + result.getValue());
        } else {
            return ResponseEntity.badRequest().body("Error registering user");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable UUID id) {
        GetUserQuery query = new GetUserQuery(id);

        Result<UserResponse> result = mediator.send(query);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getValue());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable UUID id, @RequestBody UpdateUserRequest request) {
        UpdateUserCommand command = new UpdateUserCommand(
            id,
            request.firstName(),
            request.lastName()
        );

        Result<Void> result = mediator.send(command);

        if (result.isSuccess()) {
            return ResponseEntity.ok("User updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Error updating user");
        }
    }

    @GetMapping("/permissions/{identityId}")
    public ResponseEntity<?> getUserPermissions(@PathVariable String identityId) {
        GetUserPermissionsQuery query = new GetUserPermissionsQuery(identityId);

        Result<PermissionsResponse> result = mediator.send(query);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getValue());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public record RegisterUserRequest(
        String email,
        String firstName,
        String lastName,
        String identityId) {
    }

    public record UpdateUserRequest(
        String firstName,
        String lastName) {
    }
}