package com.evently.modules.events.application.events.getevents;

import com.evently.common.application.messaging.IQueryHandler;
import com.evently.common.domain.Result;
import com.evently.modules.events.domain.events.Event;
import com.evently.modules.events.domain.events.IEventRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetEventsQueryHandler implements IQueryHandler<GetEventsQuery, List<Event>> {

    private final IEventRepository eventRepository;

    public GetEventsQueryHandler(IEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Result<List<Event>> handle(GetEventsQuery query) {
        List<Event> events = eventRepository.getAll();
        return Result.success(events);
    }
}