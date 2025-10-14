package com.evently.modules.ticketing.application.orders.getorders;

import com.evently.common.application.messaging.IQueryHandler;
import com.evently.common.domain.Result;
import com.evently.modules.ticketing.domain.orders.IOrderRepository;
import com.evently.modules.ticketing.domain.orders.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetOrdersQueryHandler implements IQueryHandler<GetOrdersQuery, List<Order>> {

    private final IOrderRepository orderRepository;

    public GetOrdersQueryHandler(IOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Result<List<Order>> handle(GetOrdersQuery query) {
        List<Order> orders = orderRepository.getAll();
        return Result.success(orders);
    }
}