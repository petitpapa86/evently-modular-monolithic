package com.evently.modules.events.integrationevents;

import com.evently.common.application.IntegrationEvent;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class EventPublishedIntegrationEvent extends IntegrationEvent {
    private UUID eventId;
    private String title;
    private String description;
    private String location;
    private LocalDateTime startsAtUtc;
    private LocalDateTime endsAtUtc;
    private List<TicketTypeModel> ticketTypes;

    public EventPublishedIntegrationEvent() {
    }

    public EventPublishedIntegrationEvent(UUID id, Instant occurredOnUtc, UUID eventId, String title,
                                        String description, String location, LocalDateTime startsAtUtc,
                                        LocalDateTime endsAtUtc, List<TicketTypeModel> ticketTypes) {
        super(id, occurredOnUtc);
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.startsAtUtc = startsAtUtc;
        this.endsAtUtc = endsAtUtc;
        this.ticketTypes = ticketTypes;
    }

    public EventPublishedIntegrationEvent(UUID eventId, String title, String description, String location,
                                        LocalDateTime startsAtUtc, LocalDateTime endsAtUtc, List<TicketTypeModel> ticketTypes) {
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.startsAtUtc = startsAtUtc;
        this.endsAtUtc = endsAtUtc;
        this.ticketTypes = ticketTypes;
    }

    // Getters and setters
    public UUID getEventId() { return eventId; }
    public void setEventId(UUID eventId) { this.eventId = eventId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDateTime getStartsAtUtc() { return startsAtUtc; }
    public void setStartsAtUtc(LocalDateTime startsAtUtc) { this.startsAtUtc = startsAtUtc; }

    public LocalDateTime getEndsAtUtc() { return endsAtUtc; }
    public void setEndsAtUtc(LocalDateTime endsAtUtc) { this.endsAtUtc = endsAtUtc; }

    public List<TicketTypeModel> getTicketTypes() { return ticketTypes; }
    public void setTicketTypes(List<TicketTypeModel> ticketTypes) { this.ticketTypes = ticketTypes; }
}