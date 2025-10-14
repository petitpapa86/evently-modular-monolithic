package com.evently.modules.events.application.events.getevents;

import com.evently.common.application.messaging.IQuery;
import com.evently.modules.events.domain.events.Event;
import java.util.List;

public record GetEventsQuery() implements IQuery<List<Event>> {
}