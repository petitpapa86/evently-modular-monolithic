package com.evently.modules.ticketing.application.customers;

import java.util.UUID;

public record CreateCustomerCommand(
    UUID userId,
    String email,
    String firstName,
    String lastName
) {
}