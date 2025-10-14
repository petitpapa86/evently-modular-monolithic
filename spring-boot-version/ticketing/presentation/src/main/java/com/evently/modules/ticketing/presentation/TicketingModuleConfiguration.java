package com.evently.modules.ticketing.presentation;

import com.evently.common.application.Mediator;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.modules.ticketing.application.orders.additemtocart.AddItemToCartCommand;
import com.evently.modules.ticketing.application.orders.additemtocart.AddItemToCartCommandHandler;
import com.evently.modules.ticketing.application.orders.createorder.CreateOrderCommand;
import com.evently.modules.ticketing.application.orders.createorder.CreateOrderCommandHandler;
import com.evently.modules.ticketing.application.orders.processpayment.ProcessPaymentCommand;
import com.evently.modules.ticketing.application.orders.processpayment.ProcessPaymentCommandHandler;
import com.evently.modules.ticketing.presentation.ticketing.AddToCart;
import com.evently.modules.ticketing.presentation.ticketing.ProcessPayment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import jakarta.annotation.PostConstruct;

import java.util.List;

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

    @Bean
    public AddToCart addToCartEndpoint() {
        return new AddToCart(mediator);
    }

    @Bean
    public ProcessPayment processPaymentEndpoint() {
        return new ProcessPayment(mediator);
    }

    @Bean
    public RouterFunction<ServerResponse> ticketingEndpoints(List<IEndpoint> endpoints) {
        return endpoints.stream()
                .filter(endpoint -> endpoint.getClass().getPackageName().startsWith("com.evently.modules.ticketing"))
                .map(IEndpoint::mapEndpoint)
                .reduce(RouterFunction::and)
                .orElseThrow(() -> new IllegalStateException("No ticketing endpoints found"));
    }
}