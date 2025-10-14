package com.evently.modules.ticketing.application.orders.createorder;

import com.evently.common.application.ICommand;
import java.util.UUID;

public record CreateOrderCommand(
    UUID customerId) implements ICommand<Void> {
}