package com.evently.common.presentation.endpoints;

import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

public interface IEndpoint {
    RouterFunction<ServerResponse> mapEndpoint();
}