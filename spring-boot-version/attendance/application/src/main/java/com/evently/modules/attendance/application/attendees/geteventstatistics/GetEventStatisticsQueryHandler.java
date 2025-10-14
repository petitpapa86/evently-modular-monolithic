package com.evently.modules.attendance.application.attendees.geteventstatistics;

import com.evently.common.application.messaging.IQueryHandler;
import com.evently.common.domain.Result;
import com.evently.modules.attendance.application.abstractions.data.ITicketRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Component
@Validated
public class GetEventStatisticsQueryHandler implements IQueryHandler<GetEventStatisticsQuery, EventStatisticsResponse> {

    private final ITicketRepository ticketRepository;

    public GetEventStatisticsQueryHandler(ITicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Result<EventStatisticsResponse> handle(GetEventStatisticsQuery query) {
        List<com.evently.modules.attendance.domain.tickets.Ticket> allTickets =
            ticketRepository.findTicketsByEventId(query.eventId());

        List<com.evently.modules.attendance.domain.tickets.Ticket> checkedInTickets =
            ticketRepository.findCheckedInTicketsByEventId(query.eventId());

        EventStatisticsResponse response = new EventStatisticsResponse(
                query.eventId(),
                allTickets.size(),
                checkedInTickets.size()
        );

        return Result.success(response);
    }
}