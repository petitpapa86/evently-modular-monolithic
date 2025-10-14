package com.evently.modules.ticketing.application.orders.getorders;

import com.evently.common.application.messaging.IQuery;
import com.evently.modules.ticketing.domain.orders.Order;

import java.util.List;

public record GetOrdersQuery() implements IQuery<List<Order>> {
}