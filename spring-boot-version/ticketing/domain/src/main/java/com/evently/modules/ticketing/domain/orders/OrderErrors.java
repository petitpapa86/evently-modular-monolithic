package com.evently.modules.ticketing.domain.orders;

import com.evently.common.domain.Error;

import java.util.UUID;

public class OrderErrors {

    public static Error notFound(UUID orderId) {
        return Error.notFound(
            "Order.NotFound",
            "The order with the identifier " + orderId + " was not found"
        );
    }

    public static Error ticketsAlreadyIssued() {
        return Error.failure(
            "Order.TicketsAlreadyIssued",
            "The tickets for this order have already been issued"
        );
    }
}