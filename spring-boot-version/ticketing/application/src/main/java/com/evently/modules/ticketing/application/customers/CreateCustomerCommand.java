package com.evently.modules.ticketing.application.customers;

import com.evently.common.application.ICommand;

import java.util.UUID;

public record CreateCustomerCommand(
    UUID userId,
    String email,
    String firstName,
    String lastName
) implements ICommand<Void> {
}