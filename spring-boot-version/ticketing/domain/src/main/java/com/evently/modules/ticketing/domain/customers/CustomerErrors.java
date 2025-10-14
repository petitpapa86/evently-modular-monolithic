package com.evently.modules.ticketing.domain.customers;

import com.evently.common.domain.Error;

import java.util.UUID;

public class CustomerErrors {

    public static Error notFound(UUID customerId) {
        return Error.notFound(
            "Customer.NotFound",
            "The customer with the identifier " + customerId + " was not found"
        );
    }
}