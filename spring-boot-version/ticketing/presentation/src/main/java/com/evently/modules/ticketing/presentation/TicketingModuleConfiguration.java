package com.evently.modules.ticketing.presentation;

import com.evently.common.application.Mediator;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.modules.ticketing.application.orders.additemtocart.AddItemToCartCommand;
import com.evently.modules.ticketing.application.orders.additemtocart.AddItemToCartCommandHandler;
import com.evently.modules.ticketing.application.orders.createorder.CreateOrderCommand;
import com.evently.modules.ticketing.application.orders.createorder.CreateOrderCommandHandler;
import com.evently.modules.ticketing.application.orders.getorder.GetOrderQuery;
import com.evently.modules.ticketing.application.orders.getorder.GetOrderQueryHandler;
import com.evently.modules.ticketing.application.orders.getorders.GetOrdersQuery;
import com.evently.modules.ticketing.application.orders.getorders.GetOrdersQueryHandler;
import com.evently.modules.ticketing.application.orders.processpayment.ProcessPaymentCommand;
import com.evently.modules.ticketing.application.orders.processpayment.ProcessPaymentCommandHandler;
import com.evently.modules.ticketing.application.orders.updateorder.UpdateOrderCommand;
import com.evently.modules.ticketing.application.orders.updateorder.UpdateOrderCommandHandler;
import com.evently.modules.ticketing.presentation.ticketing.AddToCart;
import com.evently.modules.ticketing.presentation.ticketing.CreateOrder;
import com.evently.modules.ticketing.presentation.ticketing.GetOrder;
import com.evently.modules.ticketing.presentation.ticketing.GetOrders;
import com.evently.modules.ticketing.presentation.ticketing.ProcessPayment;
import com.evently.modules.ticketing.presentation.ticketing.UpdateOrder;
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
    GetOrderQueryHandler.class,
    GetOrdersQueryHandler.class,
    ProcessPaymentCommandHandler.class,
    UpdateOrderCommandHandler.class
})
public class TicketingModuleConfiguration {

    private final Mediator mediator;
    private final AddItemToCartCommandHandler addItemToCartHandler;
    private final CreateOrderCommandHandler createOrderHandler;
    private final GetOrderQueryHandler getOrderHandler;
    private final GetOrdersQueryHandler getOrdersHandler;
    private final ProcessPaymentCommandHandler processPaymentHandler;
    private final UpdateOrderCommandHandler updateOrderHandler;

    public TicketingModuleConfiguration(Mediator mediator,
                                      AddItemToCartCommandHandler addItemToCartHandler,
                                      CreateOrderCommandHandler createOrderHandler,
                                      GetOrderQueryHandler getOrderHandler,
                                      GetOrdersQueryHandler getOrdersHandler,
                                      ProcessPaymentCommandHandler processPaymentHandler,
                                      UpdateOrderCommandHandler updateOrderHandler) {
        this.mediator = mediator;
        this.addItemToCartHandler = addItemToCartHandler;
        this.createOrderHandler = createOrderHandler;
        this.getOrderHandler = getOrderHandler;
        this.getOrdersHandler = getOrdersHandler;
        this.processPaymentHandler = processPaymentHandler;
        this.updateOrderHandler = updateOrderHandler;
    }

    @PostConstruct
    public void registerHandlers() {
        mediator.registerCommandHandler(AddItemToCartCommand.class, addItemToCartHandler);
        mediator.registerCommandHandler(CreateOrderCommand.class, createOrderHandler);
        mediator.registerCommandHandler(ProcessPaymentCommand.class, processPaymentHandler);
        mediator.registerCommandHandler(UpdateOrderCommand.class, updateOrderHandler);
        mediator.registerQueryHandler(GetOrderQuery.class, getOrderHandler);
        mediator.registerQueryHandler(GetOrdersQuery.class, getOrdersHandler);
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
    public CreateOrder createOrderEndpoint() {
        return new CreateOrder(mediator);
    }

    @Bean
    public GetOrder getOrderEndpoint() {
        return new GetOrder(mediator);
    }

    @Bean
    public GetOrders getOrdersEndpoint() {
        return new GetOrders(mediator);
    }

    @Bean
    public UpdateOrder updateOrderEndpoint() {
        return new UpdateOrder(mediator);
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