package com.evently.modules.attendance.infrastructure.attendees;

import com.evently.modules.attendance.application.abstractions.data.IAttendeeRepository;
import com.evently.modules.attendance.domain.attendees.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendeeRepository extends JpaRepository<AttendeeEntity, UUID>, IAttendeeRepository {
    @Override
    default void insert(Attendee attendee) {
        save(AttendeeEntity.fromDomain(attendee));
    }

    @Override
    default Attendee findAttendeeById(UUID id) {
        Optional<AttendeeEntity> entity = findById(id);
        return entity.map(AttendeeEntity::toDomain).orElse(null);
    }
}