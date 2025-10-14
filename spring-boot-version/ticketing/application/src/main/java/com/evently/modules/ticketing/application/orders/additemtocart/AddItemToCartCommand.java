package com.evently.modules.ticketing.application.orders.additemtocart;

import com.evently.common.application.ICommand;
import java.math.BigDecimal;
import java.util.UUID;

public record AddItemToCartCommand(
    UUID customerId,
    UUID ticketTypeId,
    BigDecimal quantity) implements ICommand<Void> {
}