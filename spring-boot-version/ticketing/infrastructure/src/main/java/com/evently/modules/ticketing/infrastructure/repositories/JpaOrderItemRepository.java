package com.evently.modules.ticketing.infrastructure.repositories;

import com.evently.modules.ticketing.infrastructure.entities.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface JpaOrderItemRepository extends JpaRepository<OrderItemEntity, UUID> {
}