package com.evently.modules.ticketing.infrastructure.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "TicketingEventEntity")
@Table(name = "events")
public class EventEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalDateTime startsAtUtc;

    @Column(nullable = false)
    private LocalDateTime endsAtUtc;

    @Column(nullable = false)
    private boolean canceled;

    @Column(nullable = false)
    private LocalDateTime createdAtUtc;

    protected EventEntity() {
    }

    public EventEntity(UUID id, String title, String description, String location,
                      LocalDateTime startsAtUtc, LocalDateTime endsAtUtc, boolean canceled,
                      LocalDateTime createdAtUtc) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.startsAtUtc = startsAtUtc;
        this.endsAtUtc = endsAtUtc;
        this.canceled = canceled;
        this.createdAtUtc = createdAtUtc;
    }

    // Getters
    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public LocalDateTime getStartsAtUtc() { return startsAtUtc; }
    public LocalDateTime getEndsAtUtc() { return endsAtUtc; }
    public boolean isCanceled() { return canceled; }
    public LocalDateTime getCreatedAtUtc() { return createdAtUtc; }
}