package com.evently.modules.ticketing.presentation.ticketing;

import com.evently.common.application.IMediator;
import com.evently.common.domain.Result;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.modules.ticketing.application.orders.getorder.GetOrderQuery;
import com.evently.modules.ticketing.domain.orders.Order;
import com.evently.modules.ticketing.domain.orders.OrderStatus;
import com.evently.modules.ticketing.presentation.Permissions;
import com.evently.modules.ticketing.presentation.Tags;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.RequestPredicates.GET;

public class GetOrder implements IEndpoint {

    private final IMediator mediator;

    public GetOrder(IMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public RouterFunction<ServerResponse> mapEndpoint() {
        return route(GET("/orders/{id}"), this::handle)
                .withAttribute("tags", new String[]{Tags.Orders})
                .withAttribute("permissions", new String[]{Permissions.GetOrder});
    }

    private ServerResponse handle(ServerRequest request) {
        try {
            UUID orderId = UUID.fromString(request.pathVariable("id"));
            GetOrderQuery query = new GetOrderQuery(orderId);

            Result<Optional<Order>> result = mediator.send(query);

            return result.match(
                    orderOpt -> {
                        if (orderOpt.isEmpty()) {
                            return ServerResponse.notFound().build();
                        }

                        Order order = orderOpt.get();
                        OrderResponse response = new OrderResponse(
                                order.getId(),
                                order.getCustomerId(),
                                order.getStatus(),
                                order.getTotalPrice(),
                                order.getCurrency(),
                                order.isTicketsIssued(),
                                order.getCreatedAtUtc(),
                                order.getOrderItems().stream()
                                        .map(item -> new OrderItemResponse(
                                                item.getId(),
                                                item.getTicketTypeId(),
                                                item.getQuantity(),
                                                item.getUnitPrice(),
                                                item.getCurrency()
                                        ))
                                        .toList()
                        );
                        return ServerResponse.ok().body(response);
                    },
                    () -> ServerResponse.status(500).body("Internal server error")
            );
        } catch (IllegalArgumentException e) {
            return ServerResponse.status(400).body("Invalid order ID format");
        } catch (Exception e) {
            return ServerResponse.status(500).body("Internal server error");
        }
    }

    public record OrderResponse(
            UUID id,
            UUID customerId,
            OrderStatus status,
            BigDecimal totalPrice,
            String currency,
            boolean ticketsIssued,
            LocalDateTime createdAtUtc,
            List<OrderItemResponse> orderItems
    ) {}

    public record OrderItemResponse(
            UUID id,
            UUID ticketTypeId,
            BigDecimal quantity,
            BigDecimal unitPrice,
            String currency
    ) {}
}