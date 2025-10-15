package com.evently.modules.ticketing.presentation.ticketing;

import com.evently.common.application.IMediator;
import com.evently.common.domain.Result;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.common.presentation.results.ApiResults;
import com.evently.modules.ticketing.application.orders.processpayment.ProcessPaymentCommand;
import com.evently.modules.ticketing.presentation.Permissions;
import com.evently.modules.ticketing.presentation.Tags;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.RequestPredicates.POST;

public class ProcessPayment implements IEndpoint {

    private final IMediator mediator;

    public ProcessPayment(IMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public RouterFunction<ServerResponse> mapEndpoint() {
        return route(POST("/ticketing/orders/{orderId}/payment"), this::handle)
                .withAttribute("tags", new String[]{Tags.Orders})
                .withAttribute("permissions", new String[]{Permissions.ProcessPayment});
    }

    private ServerResponse handle(ServerRequest request) {
        try {
            UUID orderId = UUID.fromString(request.pathVariable("orderId"));
            ProcessPaymentRequest paymentRequest = request.body(ProcessPaymentRequest.class);

            ProcessPaymentCommand command = new ProcessPaymentCommand(
                    orderId,
                    paymentRequest.transactionId(),
                    paymentRequest.amount(),
                    paymentRequest.currency()
            );

            Result<Void> result = mediator.send(command);

            return result.match(
                    success -> ServerResponse.ok().body("Payment processed successfully"),
                    () -> ApiResults.problem(result)
            );
        } catch (Exception e) {
            return ServerResponse.status(400).body("Invalid request");
        }
    }

    public record ProcessPaymentRequest(
            @NotNull @DecimalMin("0.01") BigDecimal amount,
            @NotBlank String currency,
            @NotBlank String transactionId) {
    }
}