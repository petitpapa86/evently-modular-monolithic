package com.evently.modules.ticketing.application.orders.createorder;

import com.evently.common.application.ICommandHandler;
import com.evently.common.application.IUnitOfWork;
import com.evently.common.domain.Result;
import com.evently.modules.ticketing.domain.customers.Customer;
import com.evently.modules.ticketing.domain.customers.CustomerErrors;
import com.evently.modules.ticketing.domain.customers.ICustomerRepository;
import com.evently.modules.ticketing.domain.orders.Order;
import com.evently.modules.ticketing.domain.orders.IOrderRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
public class CreateOrderCommandHandler implements ICommandHandler<CreateOrderCommand, Void> {

    private final ICustomerRepository customerRepository;
    private final IOrderRepository orderRepository;
    private final IUnitOfWork unitOfWork;

    public CreateOrderCommandHandler(
        ICustomerRepository customerRepository,
        IOrderRepository orderRepository,
        @Qualifier("ticketingUnitOfWork") IUnitOfWork unitOfWork) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.unitOfWork = unitOfWork;
    }

    @Override
    @Transactional
    public Result<Void> handle(CreateOrderCommand request) {
        // Validate customer exists
        Optional<Customer> customerOpt = customerRepository.get(request.customerId());
        if (customerOpt.isEmpty()) {
            return Result.failure(CustomerErrors.notFound(request.customerId()));
        }

        Customer customer = customerOpt.get();

        // Check if customer already has a pending order
        Optional<Order> pendingOrderOpt = orderRepository.getPendingOrder(request.customerId());
        if (pendingOrderOpt.isPresent()) {
            return Result.failure(com.evently.common.domain.Error.failure(
                "Order.AlreadyExists",
                "Customer already has a pending order"));
        }

        // Create new order
        Order order = Order.create(customer);
        orderRepository.insert(order);

        unitOfWork.saveChanges();

        return Result.success();
    }
}