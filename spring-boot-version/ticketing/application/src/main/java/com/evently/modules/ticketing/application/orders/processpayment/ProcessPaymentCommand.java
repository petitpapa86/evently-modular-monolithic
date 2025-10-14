package com.evently.modules.ticketing.application.orders.processpayment;

import com.evently.common.application.ICommand;
import java.math.BigDecimal;
import java.util.UUID;

public record ProcessPaymentCommand(
    UUID orderId,
    String transactionId,
    BigDecimal amount,
    String currency) implements ICommand<Void> {
}