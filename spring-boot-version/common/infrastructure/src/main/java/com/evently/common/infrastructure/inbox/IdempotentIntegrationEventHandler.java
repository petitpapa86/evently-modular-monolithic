package com.evently.common.infrastructure.inbox;

import com.evently.common.application.IIntegrationEventHandler;
import com.evently.common.application.IntegrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class IdempotentIntegrationEventHandler<TIntegrationEvent extends IntegrationEvent>
        implements IIntegrationEventHandler<TIntegrationEvent> {

    private static final Logger logger = LoggerFactory.getLogger(IdempotentIntegrationEventHandler.class);

    private final IIntegrationEventHandler<TIntegrationEvent> decorated;
    private final InboxMessageConsumerRepository inboxMessageConsumerRepository;

    public IdempotentIntegrationEventHandler(
            IIntegrationEventHandler<TIntegrationEvent> decorated,
            InboxMessageConsumerRepository inboxMessageConsumerRepository) {
        this.decorated = decorated;
        this.inboxMessageConsumerRepository = inboxMessageConsumerRepository;
    }

    @Override
    @Transactional
    public void handle(TIntegrationEvent integrationEvent) {
        var inboxMessageConsumer = new InboxMessageConsumer(integrationEvent.getId(), decorated.getClass().getName());

        if (inboxMessageConsumerRepository.existsByInboxMessageIdAndName(
                inboxMessageConsumer.getInboxMessageId(),
                inboxMessageConsumer.getName())) {
            logger.info("Integration event {} already processed by handler {}",
                       integrationEvent.getId(), decorated.getClass().getSimpleName());
            return;
        }

        logger.info("Processing integration event {} with handler {}",
                   integrationEvent.getId(), decorated.getClass().getSimpleName());

        decorated.handle(integrationEvent);

        inboxMessageConsumerRepository.save(inboxMessageConsumer);

        logger.info("Successfully processed integration event {} with handler {}",
                   integrationEvent.getId(), decorated.getClass().getSimpleName());
    }

    @Override
    public Class<TIntegrationEvent> getEventType() {
        return decorated.getEventType();
    }
}