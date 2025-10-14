package com.evently.modules.events.infrastructure.tickettypes;

import com.evently.modules.events.domain.tickettypes.ITicketTypeRepository;
import com.evently.modules.events.domain.tickettypes.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

public interface TicketTypeJpaRepository extends JpaRepository<TicketTypeEntity, UUID>, ITicketTypeRepository {
    @Override
    default Optional<TicketType> get(UUID id) {
        return findById(id).map(TicketTypeEntity::toDomain);
    }

    @Override
    default void insert(TicketType ticketType) {
        save(TicketTypeEntity.fromDomain(ticketType));
    }
}