package com.evently.modules.ticketing.domain.orders;

import com.evently.common.domain.Entity;
import com.evently.common.domain.IDomainEvent;
import com.evently.common.domain.Result;
import com.evently.modules.ticketing.domain.customers.Customer;
import com.evently.modules.ticketing.domain.events.TicketType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Order extends Entity {

    private final List<OrderItem> orderItems = new ArrayList<>();

    private Order() {
    }

    private UUID id;
    private UUID customerId;
    private OrderStatus status;
    private BigDecimal totalPrice;
    private String currency;
    private boolean ticketsIssued;
    private LocalDateTime createdAtUtc;

    public static Order create(Customer customer) {
        Order order = new Order();
        order.id = UUID.randomUUID();
        order.customerId = customer.getId();
        order.status = OrderStatus.PENDING;
        order.createdAtUtc = LocalDateTime.now();
        order.raise(new OrderCreatedDomainEvent(order.id));
        return order;
    }

    public void addItem(TicketType ticketType, BigDecimal quantity, BigDecimal price, String currency) {
        OrderItem orderItem = OrderItem.create(id, ticketType.getId(), quantity, price, currency);
        orderItems.add(orderItem);

        totalPrice = orderItems.stream()
            .map(OrderItem::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.currency = currency;
    }

    public Result issueTickets() {
        if (ticketsIssued) {
            return Result.failure(OrderErrors.ticketsAlreadyIssued());
        }

        ticketsIssued = true;
        raise(new OrderTicketsIssuedDomainEvent(id));

        return Result.success();
    }

    public void updateStatus(OrderStatus newStatus) {
        this.status = newStatus;
    }

    // Getters
    public UUID getId() { return id; }
    public UUID getCustomerId() { return customerId; }
    public OrderStatus getStatus() { return status; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public String getCurrency() { return currency; }
    public boolean isTicketsIssued() { return ticketsIssued; }
    public LocalDateTime getCreatedAtUtc() { return createdAtUtc; }
    public List<OrderItem> getOrderItems() { return Collections.unmodifiableList(orderItems); }
}