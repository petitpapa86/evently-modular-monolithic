package com.evently.modules.attendance.infrastructure.attendees;

import com.evently.modules.attendance.domain.attendees.Attendee;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "attendees", schema = "attendance")
public class AttendeeEntity {
    @Id
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;

    public AttendeeEntity() {
    }

    public AttendeeEntity(UUID id, String email, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Attendee toDomain() {
        return Attendee.create(id, email, firstName, lastName);
    }

    public static AttendeeEntity fromDomain(Attendee attendee) {
        return new AttendeeEntity(attendee.getId(), attendee.getEmail(), attendee.getFirstName(), attendee.getLastName());
    }

    // getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}