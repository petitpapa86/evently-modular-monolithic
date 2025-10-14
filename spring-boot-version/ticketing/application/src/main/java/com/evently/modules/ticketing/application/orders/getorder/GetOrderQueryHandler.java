package com.evently.modules.ticketing.application.orders.getorder;

import com.evently.common.application.messaging.IQueryHandler;
import com.evently.common.domain.Result;
import com.evently.modules.ticketing.domain.orders.IOrderRepository;
import com.evently.modules.ticketing.domain.orders.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetOrderQueryHandler implements IQueryHandler<GetOrderQuery, Optional<Order>> {

    private final IOrderRepository orderRepository;

    public GetOrderQueryHandler(IOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Result<Optional<Order>> handle(GetOrderQuery query) {
        Optional<Order> order = orderRepository.get(query.orderId());
        return Result.success(order);
    }
}