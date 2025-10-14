package com.evently.modules.ticketing.application.orders.getorder;

import com.evently.common.application.messaging.IQuery;
import com.evently.modules.ticketing.domain.orders.Order;

import java.util.Optional;
import java.util.UUID;

public record GetOrderQuery(UUID orderId) implements IQuery<Optional<Order>> {
}