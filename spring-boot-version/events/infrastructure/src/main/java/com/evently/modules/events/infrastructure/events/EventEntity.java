package com.evently.modules.events.infrastructure.events;

import com.evently.modules.events.domain.events.Event;
import com.evently.modules.events.domain.events.EventStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Entity(name = "EventsEventEntity")
@Table(name = "events")
public class EventEntity {
    @Id
    private UUID id;
    private UUID categoryId;
    private String title;
    private String description;
    private String location;
    private LocalDateTime startsAtUtc;
    private LocalDateTime endsAtUtc;
    private String status;

    public EventEntity() {
    }

    public EventEntity(UUID id, UUID categoryId, String title, String description, String location, LocalDateTime startsAtUtc, LocalDateTime endsAtUtc, String status) {
        this.id = id;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.startsAtUtc = startsAtUtc;
        this.endsAtUtc = endsAtUtc;
        this.status = status;
    }

    public Event toDomain() {
        Event event = new Event();
        event.setId(id);
        event.setCategoryId(categoryId);
        event.setTitle(title);
        event.setDescription(description);
        event.setLocation(location);
        event.setStartsAtUtc(startsAtUtc);
        event.setEndsAtUtc(Optional.ofNullable(endsAtUtc));
        event.setStatus(EventStatus.valueOf(status));
        return event;
    }

    public static EventEntity fromDomain(Event event) {
        return new EventEntity(
            event.getId(),
            event.getCategoryId(),
            event.getTitle(),
            event.getDescription(),
            event.getLocation(),
            event.getStartsAtUtc(),
            event.getEndsAtUtc().orElse(null),
            event.getStatus().name()
        );
    }

    // getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getStartsAtUtc() {
        return startsAtUtc;
    }

    public void setStartsAtUtc(LocalDateTime startsAtUtc) {
        this.startsAtUtc = startsAtUtc;
    }

    public LocalDateTime getEndsAtUtc() {
        return endsAtUtc;
    }

    public void setEndsAtUtc(LocalDateTime endsAtUtc) {
        this.endsAtUtc = endsAtUtc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}