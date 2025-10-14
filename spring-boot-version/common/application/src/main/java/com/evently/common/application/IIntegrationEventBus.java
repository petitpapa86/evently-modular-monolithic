package com.evently.common.application;

public interface IIntegrationEventBus {
    void publish(IntegrationEvent integrationEvent);
}