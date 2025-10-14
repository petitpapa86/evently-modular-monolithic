package com.evently.modules.ticketing.infrastructure.repositories;

import com.evently.modules.ticketing.domain.events.ITicketTypeRepository;
import com.evently.modules.ticketing.domain.events.TicketType;
import com.evently.modules.ticketing.infrastructure.entities.TicketTypeEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class TicketTypeDomainRepository implements ITicketTypeRepository {

    private final JpaTicketTypeRepository jpaTicketTypeRepository;

    public TicketTypeDomainRepository(JpaTicketTypeRepository jpaTicketTypeRepository) {
        this.jpaTicketTypeRepository = jpaTicketTypeRepository;
    }

    @Override
    public Optional<TicketType> get(UUID id) {
        return jpaTicketTypeRepository.findById(id)
                .map(entity -> TicketType.create(
                        entity.getId(),
                        entity.getEventId(),
                        entity.getName(),
                        entity.getPrice(),
                        entity.getCurrency(),
                        entity.getQuantity()
                ));
    }

    @Override
    public void insert(TicketType ticketType) {
        TicketTypeEntity entity = new TicketTypeEntity(
                ticketType.getId(),
                ticketType.getEventId(),
                ticketType.getName(),
                ticketType.getPrice(),
                ticketType.getCurrency(),
                ticketType.getQuantity(),
                ticketType.getAvailableQuantity()
        );
        jpaTicketTypeRepository.save(entity);
    }

    @Override
    public Optional<TicketType> getWithLock(UUID id) {
        // For now, same as get - in a real implementation you'd use pessimistic locking
        return get(id);
    }

    public void update(TicketType ticketType) {
        Optional<TicketTypeEntity> existingEntity = jpaTicketTypeRepository.findById(ticketType.getId());
        if (existingEntity.isPresent()) {
            TicketTypeEntity entity = existingEntity.get();
            entity.setAvailableQuantity(ticketType.getAvailableQuantity());
            jpaTicketTypeRepository.save(entity);
        }
    }
}