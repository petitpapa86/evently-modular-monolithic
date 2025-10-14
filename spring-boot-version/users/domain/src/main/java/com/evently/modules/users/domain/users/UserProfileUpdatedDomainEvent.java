package com.evently.modules.users.domain.users;

import com.evently.common.domain.DomainEvent;
import java.util.UUID;

public class UserProfileUpdatedDomainEvent extends DomainEvent {
    private final UUID userId;
    private final String firstName;
    private final String lastName;

    public UserProfileUpdatedDomainEvent(UUID userId, String firstName, String lastName) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}