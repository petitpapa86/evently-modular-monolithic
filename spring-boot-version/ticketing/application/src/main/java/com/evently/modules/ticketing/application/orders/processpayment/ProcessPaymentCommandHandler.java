package com.evently.modules.ticketing.application.orders.processpayment;

import com.evently.common.application.ICommandHandler;
import com.evently.common.application.IUnitOfWork;
import com.evently.common.domain.Result;
import com.evently.modules.ticketing.domain.orders.Order;
import com.evently.modules.ticketing.domain.orders.OrderErrors;
import com.evently.modules.ticketing.domain.orders.IOrderRepository;
import com.evently.modules.ticketing.domain.payments.Payment;
import com.evently.modules.ticketing.domain.payments.IPaymentRepository;
import com.evently.modules.ticketing.domain.tickets.Ticket;
import com.evently.modules.ticketing.domain.events.TicketType;
import com.evently.modules.ticketing.domain.tickets.ITicketRepository;
import com.evently.modules.ticketing.domain.events.ITicketTypeRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProcessPaymentCommandHandler implements ICommandHandler<ProcessPaymentCommand, Void> {

    private final IOrderRepository orderRepository;
    private final IPaymentRepository paymentRepository;
    private final ITicketRepository ticketRepository;
    private final ITicketTypeRepository ticketTypeRepository;
    private final IUnitOfWork unitOfWork;

    public ProcessPaymentCommandHandler(
        IOrderRepository orderRepository,
        IPaymentRepository paymentRepository,
        ITicketRepository ticketRepository,
        ITicketTypeRepository ticketTypeRepository,
        @Qualifier("ticketingUnitOfWork") IUnitOfWork unitOfWork) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.ticketRepository = ticketRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.unitOfWork = unitOfWork;
    }

    @Override
    @Transactional
    public Result<Void> handle(ProcessPaymentCommand request) {
        // Get the order
        Optional<Order> orderOpt = orderRepository.get(request.orderId());
        if (orderOpt.isEmpty()) {
            return Result.failure(OrderErrors.notFound(request.orderId()));
        }

        Order order = orderOpt.get();

        // Create payment
        Payment payment = Payment.create(
            order,
            UUID.fromString(request.transactionId()),
            request.amount(),
            request.currency());

        paymentRepository.insert(payment);

        // Issue tickets for the order
        Result<Void> issueTicketsResult = order.issueTickets();
        if (issueTicketsResult.isFailure()) {
            return Result.failure(issueTicketsResult.getErrors().get(0));
        }

        // Create tickets for each order item
        order.getOrderItems().forEach(orderItem -> {
            // Get the ticket type
            Optional<TicketType> ticketTypeOpt = ticketTypeRepository.get(orderItem.getTicketTypeId());
            if (ticketTypeOpt.isEmpty()) {
                // This shouldn't happen in normal flow, but handle gracefully
                return;
            }
            TicketType ticketType = ticketTypeOpt.get();

            for (int i = 0; i < orderItem.getQuantity().intValue(); i++) {
                Ticket ticket = Ticket.create(order, ticketType);
                ticketRepository.insert(ticket);
            }

            // Update ticket type quantity
            Result<Void> updateResult = ticketType.updateQuantity(orderItem.getQuantity());
            if (updateResult.isFailure()) {
                // This shouldn't happen in normal flow, but handle gracefully
                return;
            }
        });

        unitOfWork.saveChanges();

        return Result.success();
    }
}