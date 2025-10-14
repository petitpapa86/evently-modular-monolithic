package com.evently.modules.ticketing.domain.payments;

import com.evently.common.domain.Error;

public class PaymentErrors {

    public static Error alreadyRefunded() {
        return Error.failure(
            "Payment.AlreadyRefunded",
            "The payment has already been fully refunded"
        );
    }

    public static Error notEnoughFunds() {
        return Error.failure(
            "Payment.NotEnoughFunds",
            "The refund amount exceeds the available funds"
        );
    }
}