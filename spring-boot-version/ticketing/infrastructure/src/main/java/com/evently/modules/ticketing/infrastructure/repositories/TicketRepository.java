package com.evently.modules.ticketing.infrastructure.repositories;

import com.evently.modules.ticketing.domain.tickets.ITicketRepository;
import com.evently.modules.ticketing.domain.tickets.Ticket;
import com.evently.modules.ticketing.infrastructure.entities.TicketEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository("ticketingTicketRepository")
public class TicketRepository implements ITicketRepository {

    private final JpaTicketRepository jpaTicketRepository;

    public TicketRepository(JpaTicketRepository jpaTicketRepository) {
        this.jpaTicketRepository = jpaTicketRepository;
    }

    @Override
    public Optional<Ticket> get(UUID id) {
        // Simplified for now - would need proper domain reconstruction
        return Optional.empty();
    }

    @Override
    public void insert(Ticket ticket) {
        TicketEntity entity = new TicketEntity(
                ticket.getId(),
                ticket.getCustomerId(),
                ticket.getOrderId(),
                ticket.getEventId(),
                ticket.getTicketTypeId(),
                ticket.getCode(),
                ticket.getCreatedAtUtc(),
                ticket.isArchived()
        );
        jpaTicketRepository.save(entity);
    }

    public void update(Ticket ticket) {
        Optional<TicketEntity> existingEntity = jpaTicketRepository.findById(ticket.getId());
        if (existingEntity.isPresent()) {
            TicketEntity entity = existingEntity.get();
            entity.setArchived(ticket.isArchived());
            jpaTicketRepository.save(entity);
        }
    }
}