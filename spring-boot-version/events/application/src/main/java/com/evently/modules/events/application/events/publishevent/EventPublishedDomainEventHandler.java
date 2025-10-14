package com.evently.modules.events.application.events.publishevent;

import com.evently.common.application.ICommandHandler;
import com.evently.common.application.IDomainEventHandler;
import com.evently.common.application.IIntegrationEventBus;
import com.evently.common.domain.Result;
import com.evently.modules.events.domain.events.Event;
import com.evently.modules.events.domain.events.EventPublishedDomainEvent;
import com.evently.modules.events.integrationevents.EventPublishedIntegrationEvent;
import com.evently.modules.events.integrationevents.TicketTypeModel;
import com.evently.modules.events.application.events.getevent.GetEventQuery;
import com.evently.modules.events.application.events.getevent.GetEventQueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EventPublishedDomainEventHandler implements IDomainEventHandler<EventPublishedDomainEvent> {

    private static final Logger logger = LoggerFactory.getLogger(EventPublishedDomainEventHandler.class);

    private final IIntegrationEventBus integrationEventBus;
    private final GetEventQueryHandler getEventHandler;

    public EventPublishedDomainEventHandler(IIntegrationEventBus integrationEventBus, GetEventQueryHandler getEventHandler) {
        this.integrationEventBus = integrationEventBus;
        this.getEventHandler = getEventHandler;
    }

    @Override
    public void handle(EventPublishedDomainEvent event) {
        logger.info("Handling EventPublishedDomainEvent for event: {}", event.getEventId());

        // Get the complete event details
        Result<Optional<Event>> result = getEventHandler.handle(new GetEventQuery(event.getEventId()));
        if (result.isFailure()) {
            logger.error("Failed to get event details for event {}: {}", event.getEventId(), result.getErrors());
            return;
        }

        Optional<Event> eventOpt = result.getValue();
        if (eventOpt.isEmpty()) {
            logger.error("Event not found: {}", event.getEventId());
            return;
        }

        Event eventData = eventOpt.get();

        // For now, create empty list of ticket types - TODO: implement proper ticket type retrieval
        List<TicketTypeModel> ticketTypeModels = List.of();

        // Create and publish integration event
        var integrationEvent = new EventPublishedIntegrationEvent(
            event.getEventId(),
            eventData.getTitle(),
            eventData.getDescription(),
            eventData.getLocation(),
            eventData.getStartsAtUtc(),
            eventData.getEndsAtUtc().orElse(null),
            ticketTypeModels
        );

        integrationEventBus.publish(integrationEvent);
    }

    @Override
    public Class<EventPublishedDomainEvent> getEventType() {
        return EventPublishedDomainEvent.class;
    }
}