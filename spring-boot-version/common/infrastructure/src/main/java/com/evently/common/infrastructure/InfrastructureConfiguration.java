package com.evently.common.infrastructure;

import com.evently.common.application.IEventBus;
import com.evently.common.application.IDomainEventHandler;
import com.evently.common.application.IIntegrationEventBus;
import com.evently.common.application.IIntegrationEventHandler;
import com.evently.common.infrastructure.inbox.InMemoryIntegrationEventBus;
import com.evently.common.infrastructure.inbox.InboxMessageConsumerRepository;
import com.evently.common.infrastructure.inbox.IdempotentIntegrationEventHandler;
import com.evently.common.infrastructure.outbox.DomainEventDispatcher;
import com.evently.common.infrastructure.outbox.OutboxEventBus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import org.springframework.beans.factory.config.BeanPostProcessor;

import jakarta.annotation.PostConstruct;
import java.util.List;

import org.springframework.context.annotation.Lazy;

import org.springframework.context.ApplicationListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableScheduling
@EnableJpaRepositories(basePackages = {
    "com.evently.common.infrastructure.outbox",
    "com.evently.common.infrastructure.inbox"
})
@EntityScan(basePackages = {
    "com.evently.common.infrastructure.outbox",
    "com.evently.common.infrastructure.inbox"
})
public class InfrastructureConfiguration implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private DomainEventDispatcher eventDispatcher;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // Get the application context to get the beans
        var context = event.getApplicationContext();
        var eventHandlers = context.getBeansOfType(IDomainEventHandler.class).values();
        for (IDomainEventHandler<?> handler : eventHandlers) {
            eventDispatcher.registerHandler(handler);
        }
    }

    @Bean
    public IIntegrationEventBus integrationEventBus(
            ApplicationEventPublisher applicationEventPublisher,
            @Lazy List<IIntegrationEventHandler<?>> handlers) {
        return new InMemoryIntegrationEventBus(applicationEventPublisher, handlers);
    }

    @Bean
    public BeanPostProcessor idempotentIntegrationEventHandlerPostProcessor(InboxMessageConsumerRepository inboxRepository) {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) {
                if (bean instanceof IIntegrationEventHandler) {
                    return new IdempotentIntegrationEventHandler<>((IIntegrationEventHandler<?>) bean, inboxRepository);
                }
                return bean;
            }
        };
    }
}