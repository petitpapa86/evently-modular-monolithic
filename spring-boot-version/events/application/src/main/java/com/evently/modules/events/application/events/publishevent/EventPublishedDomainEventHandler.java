package com.evently.modules.events.application.events.publishevent;

import com.evently.common.application.ICommandHandler;
import com.evently.common.application.IDomainEventHandler;
import com.evently.common.application.IIntegrationEventBus;
import com.evently.common.domain.Result;
import com.evently.modules.events.domain.events.EventPublishedDomainEvent;
import com.evently.modules.events.integrationevents.EventPublishedIntegrationEvent;
import com.evently.modules.events.integrationevents.TicketTypeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
        Result<GetEventResponse> result = getEventHandler.handle(new GetEventQuery(event.getEventId()));
        if (result.isFailure()) {
            logger.error("Failed to get event details for event {}: {}", event.getEventId(), result.getErrors());
            return;
        }

        GetEventResponse eventData = result.getValue();

        // Convert ticket types to integration event model
        List<TicketTypeModel> ticketTypeModels = eventData.ticketTypes().stream()
            .map(tt -> new TicketTypeModel(
                tt.id(),
                event.getEventId(),
                tt.name(),
                BigDecimal.valueOf(tt.price()),
                tt.currency(),
                BigDecimal.valueOf(tt.quantity())
            ))
            .collect(Collectors.toList());

        // Create and publish integration event
        var integrationEvent = new EventPublishedIntegrationEvent(
            event.getEventId(),
            eventData.title(),
            eventData.description(),
            eventData.location(),
            LocalDateTime.parse(eventData.startsAtUtc()),
            eventData.endsAtUtc() != null ? LocalDateTime.parse(eventData.endsAtUtc()) : null,
            ticketTypeModels
        );

        integrationEventBus.publish(integrationEvent);
    }

    @Override
    public Class<EventPublishedDomainEvent> getEventType() {
        return EventPublishedDomainEvent.class;
    }
}