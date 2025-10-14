package com.evently.modules.ticketing.application.tickets;

import com.evently.common.application.IDomainEventHandler;
import com.evently.common.domain.Result;
import com.evently.modules.ticketing.domain.tickets.Ticket;
import com.evently.modules.ticketing.domain.tickets.TicketCreatedDomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class TicketCreatedDomainEventHandler implements IDomainEventHandler<TicketCreatedDomainEvent> {

    private static final Logger logger = LoggerFactory.getLogger(TicketCreatedDomainEventHandler.class);

    private final ApplicationEventPublisher eventPublisher;
    private final com.evently.modules.ticketing.application.abstractions.data.ITicketRepository ticketRepository;

    public TicketCreatedDomainEventHandler(
            ApplicationEventPublisher eventPublisher,
            com.evently.modules.ticketing.application.abstractions.data.ITicketRepository ticketRepository) {
        this.eventPublisher = eventPublisher;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Result<Void> handle(TicketCreatedDomainEvent event) {
        logger.info("Handling TicketCreatedDomainEvent for ticket: {}", event.ticketId());

        // Get the ticket details
        var ticketOpt = ticketRepository.get(event.ticketId());
        if (ticketOpt.isEmpty()) {
            logger.error("Ticket not found: {}", event.ticketId());
            return Result.failure();
        }

        Ticket ticket = ticketOpt.get();

        // Publish integration event for attendance module
        var integrationEvent = new TicketPurchasedIntegrationEvent(
                ticket.getId(),
                ticket.getCustomerId(),
                ticket.getEventId(),
                ticket.getCode()
        );

        eventPublisher.publishEvent(integrationEvent);
        logger.info("Published TicketPurchasedIntegrationEvent for ticket: {}", ticket.getId());

        return Result.success();
    }

    @Override
    public Class<TicketCreatedDomainEvent> getEventType() {
        return TicketCreatedDomainEvent.class;
    }
}