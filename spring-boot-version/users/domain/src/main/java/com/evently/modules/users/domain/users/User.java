package com.evently.modules.users.domain.users;

import com.evently.common.domain.Entity;
import com.evently.common.domain.Result;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class User extends Entity {
    public User() {
    }

    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String identityId;
    private final List<Role> roles = new ArrayList<>();

    public static Result<User> create(String email, String firstName, String lastName, String identityId) {
        User user = new User();
        user.id = UUID.randomUUID();
        user.email = email;
        user.firstName = firstName;
        user.lastName = lastName;
        user.identityId = identityId;

        user.roles.add(Role.MEMBER);

        user.raise(new UserRegisteredDomainEvent(user.id));

        return Result.success(user);
    }

    public void update(String firstName, String lastName) {
        if (this.firstName.equals(firstName) && this.lastName.equals(lastName)) {
            return;
        }

        this.firstName = firstName;
        this.lastName = lastName;

        raise(new UserProfileUpdatedDomainEvent(id, firstName, lastName));
    }

    public void addRole(Role role) {
        if (!roles.contains(role)) {
            roles.add(role);
            raise(new UserRolesUpdatedDomainEvent(id, roles));
        }
    }

    public void removeRole(Role role) {
        if (roles.remove(role)) {
            raise(new UserRolesUpdatedDomainEvent(id, roles));
        }
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getIdentityId() {
        return identityId;
    }

    public List<Role> getRoles() {
        return Collections.unmodifiableList(roles);
    }

    public List<Permission> getPermissions() {
        return roles.stream()
            .flatMap(role -> role.getPermissions().stream())
            .distinct()
            .collect(Collectors.toList());
    }

    public boolean hasPermission(Permission permission) {
        return getPermissions().contains(permission);
    }

    // Setters for JPA
    public void setId(UUID id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public void setRoles(List<Role> roles) {
        this.roles.clear();
        this.roles.addAll(roles);
    }
}