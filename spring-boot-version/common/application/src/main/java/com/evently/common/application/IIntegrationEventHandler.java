package com.evently.common.application;

public interface IIntegrationEventHandler<T extends IntegrationEvent> {
    void handle(T event);
    Class<T> getEventType();
}