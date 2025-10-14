package com.evently.modules.events.infrastructure.events;

import com.evently.modules.events.domain.events.Event;
import com.evently.modules.events.domain.events.IEventRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<EventEntity, UUID>, IEventRepository {
    @Override
    default Optional<Event> get(UUID id) {
        return findById(id).map(EventEntity::toDomain);
    }

    @Override
    default void insert(Event event) {
        save(EventEntity.fromDomain(event));
    }

    @Override
    default List<Event> getAll() {
        return findAll().stream().map(EventEntity::toDomain).toList();
    }
}