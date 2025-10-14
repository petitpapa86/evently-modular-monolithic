package com.evently.modules.ticketing.presentation.ticketing;

import com.evently.common.application.IMediator;
import com.evently.common.domain.Result;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.common.presentation.results.ApiResults;
import com.evently.modules.ticketing.application.orders.additemtocart.AddItemToCartCommand;
import com.evently.modules.ticketing.presentation.Permissions;
import com.evently.modules.ticketing.presentation.Tags;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.math.BigDecimal;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.RequestPredicates.POST;

public class AddToCart implements IEndpoint {

    private final IMediator mediator;

    public AddToCart(IMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public RouterFunction<ServerResponse> mapEndpoint() {
        return route(POST("/ticketing/cart/items"), this::handle)
                .withAttribute("tags", new String[]{Tags.Carts})
                .withAttribute("permissions", new String[]{Permissions.AddToCart});
    }

    private ServerResponse handle(ServerRequest request) {
        try {
            AddItemToCartRequest addRequest = request.body(AddItemToCartRequest.class);

            AddItemToCartCommand command = new AddItemToCartCommand(
                    addRequest.customerId(),
                    addRequest.ticketTypeId(),
                    BigDecimal.valueOf(addRequest.quantity())
            );

            Result<Void> result = mediator.send(command);

            return result.match(
                    success -> ServerResponse.ok().body("Item added to cart"),
                    () -> ApiResults.problem(result)
            );
        } catch (Exception e) {
            return ServerResponse.status(400).body("Invalid request");
        }
    }

    public record AddItemToCartRequest(
            @NotNull java.util.UUID customerId,
            @NotNull java.util.UUID ticketTypeId,
            @Min(1) int quantity) {
    }
}