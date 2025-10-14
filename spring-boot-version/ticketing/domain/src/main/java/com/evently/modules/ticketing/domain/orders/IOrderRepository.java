package com.evently.modules.ticketing.domain.orders;

import java.util.Optional;
import java.util.UUID;

public interface IOrderRepository {

    Optional<Order> get(UUID orderId);

    Optional<Order> getPendingOrder(UUID customerId);

    void insert(Order order);
}