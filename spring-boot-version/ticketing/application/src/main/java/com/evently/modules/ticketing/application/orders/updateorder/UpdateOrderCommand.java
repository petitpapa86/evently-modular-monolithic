package com.evently.modules.ticketing.application.orders.updateorder;

import com.evently.common.application.ICommand;
import com.evently.modules.ticketing.domain.orders.OrderStatus;

import java.util.UUID;

public record UpdateOrderCommand(UUID orderId, OrderStatus status) implements ICommand<Void> {
}