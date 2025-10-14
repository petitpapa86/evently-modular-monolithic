package com.evently.common.infrastructure.inbox;

import com.evently.common.application.IIntegrationEventHandler;
import com.evently.common.application.IntegrationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

@Configuration
public class IntegrationEventHandlerConfiguration {

    @Autowired
    private InboxMessageConsumerRepository inboxMessageConsumerRepository;

    @Bean
    public BeanPostProcessor idempotentIntegrationEventHandlerPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) {
                if (bean instanceof IIntegrationEventHandler) {
                    return new IdempotentIntegrationEventHandler<>((IIntegrationEventHandler<IntegrationEvent>) bean, inboxMessageConsumerRepository);
                }
                return bean;
            }
        };
    }
}