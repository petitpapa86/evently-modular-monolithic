package com.evently.modules.attendance.application.abstractions.data;

import com.evently.modules.attendance.domain.tickets.Ticket;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITicketRepository {
    void insert(Ticket ticket);
    Optional<Ticket> findTicketById(UUID id);
    List<Ticket> findTicketsByEventId(UUID eventId);
    List<Ticket> findCheckedInTicketsByEventId(UUID eventId);
}