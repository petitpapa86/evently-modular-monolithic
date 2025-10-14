package com.evently.modules.events.application.events.getevent;

import com.evently.common.application.messaging.IQueryHandler;
import com.evently.common.domain.Result;
import com.evently.modules.events.domain.events.Event;
import com.evently.modules.events.domain.events.IEventRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetEventQueryHandler implements IQueryHandler<GetEventQuery, Optional<Event>> {

    private final IEventRepository eventRepository;

    public GetEventQueryHandler(IEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Result<Optional<Event>> handle(GetEventQuery query) {
        Optional<Event> event = eventRepository.get(query.eventId());
        return Result.success(event);
    }
}