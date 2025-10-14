package com.evently.modules.users.domain.users;

import com.evently.common.domain.DomainEvent;
import java.util.UUID;

public class UserRegisteredDomainEvent extends DomainEvent {
    private final UUID userId;

    public UserRegisteredDomainEvent(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }
}