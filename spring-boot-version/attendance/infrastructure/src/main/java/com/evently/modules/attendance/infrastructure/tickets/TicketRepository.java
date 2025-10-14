package com.evently.modules.attendance.infrastructure.tickets;

import com.evently.modules.attendance.application.abstractions.data.ITicketRepository;
import com.evently.modules.attendance.domain.tickets.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, UUID>, ITicketRepository {

    @Override
    default void insert(Ticket ticket) {
        save(TicketEntity.fromDomain(ticket));
    }

    @Override
    default Optional<Ticket> findTicketById(UUID id) {
        Optional<TicketEntity> entity = findById(id);
        return entity.map(TicketEntity::toDomain);
    }

    @Override
    default List<Ticket> findTicketsByEventId(UUID eventId) {
        List<TicketEntity> entities = findByEventId(eventId);
        return entities.stream().map(TicketEntity::toDomain).toList();
    }

    @Override
    default List<Ticket> findCheckedInTicketsByEventId(UUID eventId) {
        List<TicketEntity> entities = findByEventIdAndUsedAtUtcIsNotNull(eventId);
        return entities.stream().map(TicketEntity::toDomain).toList();
    }

    List<TicketEntity> findByEventId(UUID eventId);

    List<TicketEntity> findByEventIdAndUsedAtUtcIsNotNull(UUID eventId);
}