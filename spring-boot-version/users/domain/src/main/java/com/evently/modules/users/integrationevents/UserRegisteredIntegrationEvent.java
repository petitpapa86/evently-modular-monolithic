package com.evently.modules.users.integrationevents;

import com.evently.common.application.IntegrationEvent;

import java.time.Instant;
import java.util.UUID;

public class UserRegisteredIntegrationEvent extends IntegrationEvent {
    private final UUID userId;
    private final String email;
    private final String firstName;
    private final String lastName;

    public UserRegisteredIntegrationEvent(UUID id, Instant occurredOnUtc, UUID userId, String email, String firstName, String lastName) {
        super(id, occurredOnUtc);
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserRegisteredIntegrationEvent(UUID userId, String email, String firstName, String lastName) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UUID getUserId() {
        return userId;
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
}