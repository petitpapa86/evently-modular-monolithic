package com.evently.modules.ticketing.infrastructure.repositories;

import com.evently.modules.ticketing.infrastructure.entities.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface JpaEventRepository extends JpaRepository<EventEntity, UUID> {
}