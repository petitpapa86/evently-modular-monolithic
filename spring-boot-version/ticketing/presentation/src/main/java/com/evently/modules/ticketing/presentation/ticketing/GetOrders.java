package com.evently.modules.ticketing.presentation.ticketing;

import com.evently.common.application.IMediator;
import com.evently.common.domain.Result;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.modules.ticketing.application.orders.getorders.GetOrdersQuery;
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

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.RequestPredicates.GET;

public class GetOrders implements IEndpoint {

    private final IMediator mediator;

    public GetOrders(IMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public RouterFunction<ServerResponse> mapEndpoint() {
        return route(GET("/orders"), this::handle)
                .withAttribute("tags", new String[]{Tags.Orders})
                .withAttribute("permissions", new String[]{Permissions.GetOrders});
    }

    private ServerResponse handle(ServerRequest request) {
        try {
            GetOrdersQuery query = new GetOrdersQuery();

            Result<List<Order>> result = mediator.send(query);

            return result.match(
                    orders -> {
                        List<GetOrder.OrderResponse> responses = orders.stream()
                                .map(order -> new GetOrder.OrderResponse(
                                        order.getId(),
                                        order.getCustomerId(),
                                        order.getStatus(),
                                        order.getTotalPrice(),
                                        order.getCurrency(),
                                        order.isTicketsIssued(),
                                        order.getCreatedAtUtc(),
                                        order.getOrderItems().stream()
                                                .map(item -> new GetOrder.OrderItemResponse(
                                                        item.getId(),
                                                        item.getTicketTypeId(),
                                                        item.getQuantity(),
                                                        item.getUnitPrice(),
                                                        item.getCurrency()
                                                ))
                                                .toList()
                                ))
                                .toList();

                        return ServerResponse.ok().body(responses);
                    },
                    () -> ServerResponse.status(500).body("Failed to retrieve orders")
            );
        } catch (Exception e) {
            return ServerResponse.status(500).body("Internal server error");
        }
    }
}