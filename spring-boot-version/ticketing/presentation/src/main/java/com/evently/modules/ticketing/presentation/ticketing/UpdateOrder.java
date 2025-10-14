package com.evently.modules.ticketing.presentation.ticketing;

import com.evently.common.application.IMediator;
import com.evently.common.domain.Result;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.modules.ticketing.application.orders.updateorder.UpdateOrderCommand;
import com.evently.modules.ticketing.domain.orders.OrderStatus;
import com.evently.modules.ticketing.presentation.Permissions;
import com.evently.modules.ticketing.presentation.Tags;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.UUID;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.RequestPredicates.PUT;

public class UpdateOrder implements IEndpoint {

    private final IMediator mediator;

    public UpdateOrder(IMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public RouterFunction<ServerResponse> mapEndpoint() {
        return route(PUT("/orders/{id}"), this::handle)
                .withAttribute("tags", new String[]{Tags.Orders})
                .withAttribute("permissions", new String[]{Permissions.UpdateOrder});
    }

    private ServerResponse handle(ServerRequest request) {
        try {
            UUID orderId = UUID.fromString(request.pathVariable("id"));

            UpdateOrderRequest updateRequest = request.body(UpdateOrderRequest.class);
            OrderStatus status = OrderStatus.valueOf(updateRequest.status().toUpperCase());

            UpdateOrderCommand command = new UpdateOrderCommand(orderId, status);

            Result<Void> result = mediator.send(command);

            return result.match(
                    success -> ServerResponse.ok().build(),
                    () -> ServerResponse.status(400).body("Failed to update order")
            );
        } catch (IllegalArgumentException e) {
            return ServerResponse.status(400).body("Invalid order ID format or status value");
        } catch (Exception e) {
            return ServerResponse.status(500).body("Internal server error");
        }
    }

    public record UpdateOrderRequest(@NotBlank String status) {}
}