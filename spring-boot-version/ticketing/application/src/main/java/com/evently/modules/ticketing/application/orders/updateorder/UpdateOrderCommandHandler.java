package com.evently.modules.ticketing.application.orders.updateorder;

import com.evently.common.application.ICommandHandler;
import com.evently.common.domain.Result;
import com.evently.modules.ticketing.domain.orders.IOrderRepository;
import com.evently.modules.ticketing.domain.orders.Order;
import com.evently.modules.ticketing.domain.orders.OrderErrors;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Component
@Validated
public class UpdateOrderCommandHandler implements ICommandHandler<UpdateOrderCommand, Void> {

    private final IOrderRepository orderRepository;

    public UpdateOrderCommandHandler(IOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Result handle(UpdateOrderCommand command) {
        Optional<Order> orderOpt = orderRepository.get(command.orderId());
        if (orderOpt.isEmpty()) {
            return Result.failure(OrderErrors.notFound(command.orderId()));
        }

        Order order = orderOpt.get();
        order.updateStatus(command.status());
        orderRepository.update(order);

        return Result.success();
    }
}