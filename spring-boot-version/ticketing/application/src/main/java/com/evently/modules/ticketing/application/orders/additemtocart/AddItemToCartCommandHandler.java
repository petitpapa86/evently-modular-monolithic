package com.evently.modules.ticketing.application.orders.additemtocart;

import com.evently.common.application.ICommandHandler;
import com.evently.common.application.IUnitOfWork;
import com.evently.common.domain.Result;
import com.evently.modules.ticketing.domain.customers.Customer;
import com.evently.modules.ticketing.domain.customers.CustomerErrors;
import com.evently.modules.ticketing.domain.customers.ICustomerRepository;
import com.evently.modules.ticketing.domain.events.TicketType;
import com.evently.modules.ticketing.domain.events.TicketTypeErrors;
import com.evently.modules.ticketing.domain.events.ITicketTypeRepository;
import com.evently.modules.ticketing.domain.orders.Order;
import com.evently.modules.ticketing.domain.orders.IOrderRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
@Validated
public class AddItemToCartCommandHandler implements ICommandHandler<AddItemToCartCommand, Void> {

    private final ICustomerRepository customerRepository;
    private final ITicketTypeRepository ticketTypeRepository;
    private final IOrderRepository orderRepository;
    private final IUnitOfWork unitOfWork;

    public AddItemToCartCommandHandler(
        ICustomerRepository customerRepository,
        ITicketTypeRepository ticketTypeRepository,
        IOrderRepository orderRepository,
        @Qualifier("ticketingUnitOfWork") IUnitOfWork unitOfWork) {
        this.customerRepository = customerRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.orderRepository = orderRepository;
        this.unitOfWork = unitOfWork;
    }

    @Override
    @Transactional
    public Result<Void> handle(AddItemToCartCommand request) {
        // Validate customer exists
        Optional<Customer> customerOpt = customerRepository.get(request.customerId());
        if (customerOpt.isEmpty()) {
            return Result.failure(CustomerErrors.notFound(request.customerId()));
        }

        // Validate ticket type exists
        Optional<TicketType> ticketTypeOpt = ticketTypeRepository.get(request.ticketTypeId());
        if (ticketTypeOpt.isEmpty()) {
            return Result.failure(TicketTypeErrors.notFound(request.ticketTypeId()));
        }

        TicketType ticketType = ticketTypeOpt.get();

        // Check if ticket type has enough quantity
        Result<Void> quantityCheck = ticketType.reserveQuantity(request.quantity());
        if (quantityCheck.isFailure()) {
            return Result.failure(quantityCheck.getErrors().get(0));
        }

        // Get or create pending order for customer
        Optional<Order> pendingOrderOpt = orderRepository.getPendingOrder(request.customerId());
        Order order;

        if (pendingOrderOpt.isPresent()) {
            order = pendingOrderOpt.get();
        } else {
            Customer customer = customerOpt.get();
            order = Order.create(customer);
            orderRepository.insert(order);
        }

        // Add item to order
        BigDecimal price = ticketType.getPrice().multiply(request.quantity());
        order.addItem(ticketType, request.quantity(), price, ticketType.getCurrency());

        unitOfWork.saveChanges();

        return Result.success();
    }
}