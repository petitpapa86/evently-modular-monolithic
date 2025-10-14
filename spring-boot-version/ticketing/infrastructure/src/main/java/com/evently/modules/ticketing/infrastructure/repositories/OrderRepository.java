package com.evently.modules.ticketing.infrastructure.repositories;

import com.evently.modules.ticketing.domain.orders.IOrderRepository;
import com.evently.modules.ticketing.domain.orders.Order;
import com.evently.modules.ticketing.domain.orders.OrderItem;
import com.evently.modules.ticketing.infrastructure.entities.OrderEntity;
import com.evently.modules.ticketing.infrastructure.entities.OrderItemEntity;
import org.springframework.stereotype.Repository;

import com.evently.modules.ticketing.domain.orders.OrderStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class OrderRepository implements IOrderRepository {

    private final JpaOrderRepository jpaOrderRepository;
    private final JpaOrderItemRepository jpaOrderItemRepository;

    public OrderRepository(JpaOrderRepository jpaOrderRepository, JpaOrderItemRepository jpaOrderItemRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
        this.jpaOrderItemRepository = jpaOrderItemRepository;
    }

    @Override
    public Optional<Order> getPendingOrder(UUID customerId) {
        // This would require a custom query - for now return empty
        // In a real implementation, you'd query for orders with PENDING status for the customer
        return Optional.empty();
    }

    @Override
    public Optional<Order> get(UUID id) {
        // Simplified for now - would need proper domain reconstruction
        return Optional.empty();
    }

    @Override
    public void insert(Order order) {
        OrderEntity entity = new OrderEntity(
                order.getId(),
                order.getCustomerId(),
                order.getStatus().name(),
                order.getTotalPrice(),
                order.getCurrency(),
                order.getCreatedAtUtc()
        );

        // Save order first
        OrderEntity savedOrder = jpaOrderRepository.save(entity);

        // Save order items
        List<OrderItemEntity> orderItemEntities = order.getOrderItems().stream()
                .map(orderItem -> new OrderItemEntity(
                        orderItem.getId(),
                        savedOrder,
                        orderItem.getTicketTypeId(),
                        orderItem.getQuantity(),
                        orderItem.getUnitPrice(),
                        orderItem.getCurrency()
                ))
                .collect(Collectors.toList());

        jpaOrderItemRepository.saveAll(orderItemEntities);
    }

    public void update(Order order) {
        Optional<OrderEntity> existingEntity = jpaOrderRepository.findById(order.getId());
        if (existingEntity.isPresent()) {
            OrderEntity entity = existingEntity.get();
            entity.setStatus(order.getStatus().name());
            jpaOrderRepository.save(entity);
        }
    }
}