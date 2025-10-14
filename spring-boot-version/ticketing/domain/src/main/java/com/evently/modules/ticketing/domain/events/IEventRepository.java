package com.evently.modules.ticketing.domain.events;

import java.util.Optional;
import java.util.UUID;

public interface IEventRepository {

    Optional<Event> get(UUID eventId);

    void insert(Event event);
}