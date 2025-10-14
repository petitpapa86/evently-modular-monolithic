package com.evently.modules.attendance.application.tickets;

import com.evently.common.application.IIntegrationEventHandler;
import com.evently.common.domain.Result;
import com.evently.modules.attendance.application.abstractions.data.IAttendeeRepository;
import com.evently.modules.attendance.application.abstractions.data.ITicketRepository;
import com.evently.modules.attendance.domain.tickets.Ticket;
import com.evently.modules.ticketing.integrationevents.TicketPurchasedIntegrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TicketPurchasedIntegrationEventHandler implements IIntegrationEventHandler<TicketPurchasedIntegrationEvent> {

    private static final Logger logger = LoggerFactory.getLogger(TicketPurchasedIntegrationEventHandler.class);

    private final ITicketRepository ticketRepository;
    private final IAttendeeRepository attendeeRepository;

    public TicketPurchasedIntegrationEventHandler(
            ITicketRepository ticketRepository,
            IAttendeeRepository attendeeRepository) {
        this.ticketRepository = ticketRepository;
        this.attendeeRepository = attendeeRepository;
    }

    @EventListener
    @Override
    public void handle(TicketPurchasedIntegrationEvent event) {
        logger.info("Handling TicketPurchasedIntegrationEvent for ticket: {}", event.getTicketId());

        // Check if attendee exists (they should have been created when user registered)
        var attendeeOpt = attendeeRepository.findAttendeeById(event.getCustomerId());
        if (attendeeOpt.isEmpty()) {
            logger.error("Attendee not found for customer: {}", event.getCustomerId());
            return;
        }

        // Create attendance ticket
        Ticket ticket = new Ticket(
                event.getTicketId(),
                event.getTicketCode(),
                event.getCustomerId(),
                event.getEventId()
        );

        ticketRepository.insert(ticket);
        logger.info("Created attendance ticket for event: {}", event.getEventId());
    }

    @Override
    public Class<TicketPurchasedIntegrationEvent> getEventType() {
        return TicketPurchasedIntegrationEvent.class;
    }
}