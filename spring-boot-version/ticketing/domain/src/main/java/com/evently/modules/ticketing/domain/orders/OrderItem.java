package com.evently.modules.ticketing.domain.orders;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderItem {

    private OrderItem() {
    }

    private UUID id;
    private UUID orderId;
    private UUID ticketTypeId;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal price;
    private String currency;

    public static OrderItem create(UUID orderId, UUID ticketTypeId, BigDecimal quantity,
                                  BigDecimal unitPrice, String currency) {
        OrderItem orderItem = new OrderItem();
        orderItem.id = UUID.randomUUID();
        orderItem.orderId = orderId;
        orderItem.ticketTypeId = ticketTypeId;
        orderItem.quantity = quantity;
        orderItem.unitPrice = unitPrice;
        orderItem.price = quantity.multiply(unitPrice);
        orderItem.currency = currency;
        return orderItem;
    }

    // Getters
    public UUID getId() { return id; }
    public UUID getOrderId() { return orderId; }
    public UUID getTicketTypeId() { return ticketTypeId; }
    public BigDecimal getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public BigDecimal getPrice() { return price; }
    public String getCurrency() { return currency; }
}