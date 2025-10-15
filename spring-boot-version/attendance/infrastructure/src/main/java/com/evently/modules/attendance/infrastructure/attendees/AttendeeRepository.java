package com.evently.modules.attendance.infrastructure.attendees;

import com.evently.modules.attendance.application.abstractions.data.IAttendeeRepository;
import com.evently.modules.attendance.domain.attendees.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendeeRepository extends JpaRepository<AttendeeEntity, UUID>, IAttendeeRepository {
    @Override
    default void insert(Attendee attendee) {
        save(AttendeeEntity.fromDomain(attendee));
    }

    @Override
    default Optional<Attendee> findAttendeeById(UUID id) {
        Optional<AttendeeEntity> entity = findById(id);
        return entity.map(AttendeeEntity::toDomain);
    }

    @Override
    @Query("SELECT a FROM AttendeeEntity a")
    List<Attendee> retrieveAll();
}