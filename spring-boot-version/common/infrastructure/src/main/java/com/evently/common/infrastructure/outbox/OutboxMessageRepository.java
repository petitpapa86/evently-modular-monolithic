package com.evently.common.infrastructure.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, UUID> {

    @Query("SELECT om FROM OutboxMessage om WHERE om.status = :status ORDER BY om.occurredOnUtc ASC")
    List<OutboxMessage> findByStatusOrderByOccurredOnUtc(@Param("status") OutboxMessageStatus status);

    @Query("SELECT COUNT(om) FROM OutboxMessage om WHERE om.status = :status")
    long countByStatus(@Param("status") OutboxMessageStatus status);
}