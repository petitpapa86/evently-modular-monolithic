package com.evently.modules.ticketing.infrastructure.repositories;

import com.evently.modules.ticketing.application.abstractions.data.ITicketRepository;
import com.evently.modules.ticketing.domain.tickets.Ticket;
import com.evently.modules.ticketing.infrastructure.entities.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("ticketingTicketApplicationRepository")
public interface JpaTicketRepository extends JpaRepository<TicketEntity, UUID>, ITicketRepository {

    @Override
    default void insert(Ticket ticket) {
        save(TicketEntity.fromDomain(ticket));
    }

    @Override
    default Optional<Ticket> get(UUID ticketId) {
        Optional<TicketEntity> entity = findById(ticketId);
        return entity.map(TicketEntity::toDomain);
    }

    @Override
    default List<Ticket> findTicketsByEventId(UUID eventId) {
        List<TicketEntity> entities = findByEventId(eventId);
        return entities.stream().map(TicketEntity::toDomain).toList();
    }

    @Query("SELECT t FROM TicketingTicketEntity t WHERE t.eventId = :eventId")
    List<TicketEntity> findByEventId(@Param("eventId") UUID eventId);
}