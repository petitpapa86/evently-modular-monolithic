package com.evently.modules.ticketing.domain.orders;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IOrderRepository {

    Optional<Order> get(UUID orderId);

    Optional<Order> getPendingOrder(UUID customerId);

    List<Order> getAll();

    void insert(Order order);

    void update(Order order);
}