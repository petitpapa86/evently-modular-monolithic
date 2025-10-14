package com.evently.modules.ticketing.infrastructure.repositories;

import com.evently.modules.ticketing.domain.events.Event;
import com.evently.modules.ticketing.domain.events.IEventRepository;
import com.evently.modules.ticketing.infrastructure.entities.EventEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EventDomainRepository implements IEventRepository {

    private final JpaEventRepository jpaEventRepository;

    public EventDomainRepository(JpaEventRepository jpaEventRepository) {
        this.jpaEventRepository = jpaEventRepository;
    }

    @Override
    public Optional<Event> get(UUID id) {
        return jpaEventRepository.findById(id)
                .map(entity -> Event.create(
                        entity.getId(),
                        entity.getTitle(),
                        entity.getDescription(),
                        entity.getLocation(),
                        entity.getStartsAtUtc(),
                        entity.getEndsAtUtc()
                ));
    }

    @Override
    public void insert(Event event) {
        EventEntity entity = new EventEntity(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getLocation(),
                event.getStartsAtUtc(),
                event.getEndsAtUtc(),
                event.isCanceled(),
                LocalDateTime.now()
        );
        jpaEventRepository.save(entity);
    }
}