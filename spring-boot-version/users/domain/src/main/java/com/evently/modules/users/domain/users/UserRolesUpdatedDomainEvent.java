package com.evently.modules.users.domain.users;

import com.evently.common.domain.DomainEvent;
import java.util.List;
import java.util.UUID;

public class UserRolesUpdatedDomainEvent extends DomainEvent {
    private final UUID userId;
    private final List<Role> roles;

    public UserRolesUpdatedDomainEvent(UUID userId, List<Role> roles) {
        this.userId = userId;
        this.roles = List.copyOf(roles);
    }

    public UUID getUserId() {
        return userId;
    }

    public List<Role> getRoles() {
        return roles;
    }
}