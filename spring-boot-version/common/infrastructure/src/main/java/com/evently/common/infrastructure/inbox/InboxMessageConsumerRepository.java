package com.evently.common.infrastructure.inbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InboxMessageConsumerRepository extends JpaRepository<InboxMessageConsumer, UUID> {

    @Query("SELECT CASE WHEN COUNT(imc) > 0 THEN true ELSE false END " +
           "FROM InboxMessageConsumer imc " +
           "WHERE imc.inboxMessageId = :inboxMessageId AND imc.name = :name")
    boolean existsByInboxMessageIdAndName(@Param("inboxMessageId") UUID inboxMessageId, @Param("name") String name);
}