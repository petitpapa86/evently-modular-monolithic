package com.evently.modules.ticketing.domain.events;

import java.util.Optional;
import java.util.UUID;

public interface ITicketTypeRepository {

    Optional<TicketType> get(UUID ticketTypeId);

    Optional<TicketType> getWithLock(UUID ticketTypeId);

    void insert(TicketType ticketType);
}