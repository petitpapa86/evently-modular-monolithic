package com.evently.modules.ticketing.presentation.ticketing;

import com.evently.common.application.IMediator;
import com.evently.common.domain.Result;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.common.presentation.results.ApiResults;
import com.evently.modules.ticketing.application.orders.createorder.CreateOrderCommand;
import com.evently.modules.ticketing.presentation.Permissions;
import com.evently.modules.ticketing.presentation.Tags;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.RequestPredicates.POST;

public class CreateOrder implements IEndpoint {

    private final IMediator mediator;

    public CreateOrder(IMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public RouterFunction<ServerResponse> mapEndpoint() {
        return route(POST("/ticketing/orders"), this::handle)
                .withAttribute("tags", new String[]{Tags.Orders})
                .withAttribute("permissions", new String[]{Permissions.CreateOrder});
    }

    private ServerResponse handle(ServerRequest request) {
        try {
            CreateOrderRequest createRequest = request.body(CreateOrderRequest.class);

            CreateOrderCommand command = new CreateOrderCommand(
                    createRequest.customerId()
            );

            Result<Void> result = mediator.send(command);

            return result.match(
                    success -> ServerResponse.ok().body("Order created successfully"),
                    () -> ApiResults.problem(result)
            );
        } catch (Exception e) {
            return ServerResponse.status(400).body("Invalid request");
        }
    }

    public record CreateOrderRequest(
            java.util.UUID customerId) {
    }
}