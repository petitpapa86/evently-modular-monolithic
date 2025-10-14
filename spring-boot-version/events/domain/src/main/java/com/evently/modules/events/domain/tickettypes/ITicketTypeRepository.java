package com.evently.modules.events.domain.tickettypes;

import java.util.Optional;
import java.util.UUID;

public interface ITicketTypeRepository {
    Optional<TicketType> get(UUID id);

    void insert(TicketType ticketType);
}