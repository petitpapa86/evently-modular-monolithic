package com.evently.modules.events.domain.events;

import com.evently.common.domain.Entity;
import com.evently.common.domain.Result;
import com.evently.modules.events.domain.categories.Category;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class Event extends Entity {
    public Event() {
    }

    private UUID id;
    private UUID categoryId;
    private String title;
    private String description;
    private String location;
    private LocalDateTime startsAtUtc;
    private Optional<LocalDateTime> endsAtUtc;
    private EventStatus status;

    public static Result<Event> create(Category category, String title, String description, String location, LocalDateTime startsAtUtc, Optional<LocalDateTime> endsAtUtc) {
        if (endsAtUtc.isPresent() && endsAtUtc.get().isBefore(startsAtUtc)) {
            return Result.failure(EventErrors.END_DATE_PRECEDES_START_DATE);
        }

        Event event = new Event();
        event.id = UUID.randomUUID();
        event.categoryId = category.getId();
        event.title = title;
        event.description = description;
        event.location = location;
        event.startsAtUtc = startsAtUtc;
        event.endsAtUtc = endsAtUtc;
        event.status = EventStatus.DRAFT;

        event.raise(new EventCreatedDomainEvent(event.id));

        return Result.success(event);
    }

    public Result publish() {
        if (status != EventStatus.DRAFT) {
            return Result.failure(EventErrors.NOT_DRAFT);
        }

        status = EventStatus.PUBLISHED;

        raise(new EventPublishedDomainEvent(id));

        return Result.success();
    }

    public void reschedule(LocalDateTime startsAtUtc, Optional<LocalDateTime> endsAtUtc) {
        if (this.startsAtUtc.equals(startsAtUtc) && this.endsAtUtc.equals(endsAtUtc)) {
            return;
        }

        this.startsAtUtc = startsAtUtc;
        this.endsAtUtc = endsAtUtc;

        raise(new EventRescheduledDomainEvent(id, startsAtUtc, endsAtUtc));
    }

    public Result cancel(LocalDateTime utcNow) {
        if (status == EventStatus.CANCELED) {
            return Result.failure(EventErrors.ALREADY_CANCELED);
        }

        if (startsAtUtc.isBefore(utcNow)) {
            return Result.failure(EventErrors.ALREADY_STARTED);
        }

        status = EventStatus.CANCELED;

        raise(new EventCanceledDomainEvent(id));

        return Result.success();
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getStartsAtUtc() {
        return startsAtUtc;
    }

    public Optional<LocalDateTime> getEndsAtUtc() {
        return endsAtUtc;
    }

    public EventStatus getStatus() {
        return status;
    }

    // Setters for JPA
    public void setId(UUID id) {
        this.id = id;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStartsAtUtc(LocalDateTime startsAtUtc) {
        this.startsAtUtc = startsAtUtc;
    }

    public void setEndsAtUtc(Optional<LocalDateTime> endsAtUtc) {
        this.endsAtUtc = endsAtUtc;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }
}