package com.evently.modules.events.domain.events;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IEventRepository {
    Optional<Event> get(UUID id);

    void insert(Event event);

    List<Event> getAll();
}