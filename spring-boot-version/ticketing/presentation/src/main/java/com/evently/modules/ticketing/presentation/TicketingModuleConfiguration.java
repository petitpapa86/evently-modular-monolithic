package com.evently.modules.ticketing.presentation;

import com.evently.common.application.Mediator;
import com.evently.modules.ticketing.application.orders.additemtocart.AddItemToCartCommand;
import com.evently.modules.ticketing.application.orders.additemtocart.AddItemToCartCommandHandler;
import com.evently.modules.ticketing.application.orders.createorder.CreateOrderCommand;
import com.evently.modules.ticketing.application.orders.createorder.CreateOrderCommandHandler;
import com.evently.modules.ticketing.application.orders.processpayment.ProcessPaymentCommand;
import com.evently.modules.ticketing.application.orders.processpayment.ProcessPaymentCommandHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import jakarta.annotation.PostConstruct;

@Configuration
@Import({
    AddItemToCartCommandHandler.class,
    CreateOrderCommandHandler.class,
    ProcessPaymentCommandHandler.class
})
public class TicketingModuleConfiguration {

    private final Mediator mediator;
    private final AddItemToCartCommandHandler addItemToCartHandler;
    private final CreateOrderCommandHandler createOrderHandler;
    private final ProcessPaymentCommandHandler processPaymentHandler;

    public TicketingModuleConfiguration(Mediator mediator,
                                      AddItemToCartCommandHandler addItemToCartHandler,
                                      CreateOrderCommandHandler createOrderHandler,
                                      ProcessPaymentCommandHandler processPaymentHandler) {
        this.mediator = mediator;
        this.addItemToCartHandler = addItemToCartHandler;
        this.createOrderHandler = createOrderHandler;
        this.processPaymentHandler = processPaymentHandler;
    }

    @PostConstruct
    public void registerHandlers() {
        mediator.registerCommandHandler(AddItemToCartCommand.class, addItemToCartHandler);
        mediator.registerCommandHandler(CreateOrderCommand.class, createOrderHandler);
        mediator.registerCommandHandler(ProcessPaymentCommand.class, processPaymentHandler);
    }
}