package com.evently.modules.ticketing.application.abstractions.data;

import com.evently.modules.ticketing.domain.tickets.Ticket;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITicketRepository {
    Optional<Ticket> get(UUID ticketId);
    void insert(Ticket ticket);
    List<Ticket> findTicketsByEventId(UUID eventId);
}