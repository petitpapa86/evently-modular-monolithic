package com.evently.modules.ticketing.domain.tickets;

import java.util.Optional;
import java.util.UUID;

public interface ITicketRepository {

    Optional<Ticket> get(UUID ticketId);

    void insert(Ticket ticket);
}