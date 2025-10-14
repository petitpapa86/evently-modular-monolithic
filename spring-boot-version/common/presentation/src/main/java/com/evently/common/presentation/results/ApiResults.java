package com.evently.common.presentation.results;

import com.evently.common.domain.Error;
import com.evently.common.domain.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.ServerResponse;

public class ApiResults {
    public static ServerResponse problem(Result<?> result) {
        if (result.getErrors().isEmpty()) {
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        com.evently.common.domain.Error error = result.getErrors().get(0);
        HttpStatus status = switch (error.type()) {
            case VALIDATION -> HttpStatus.BAD_REQUEST;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case CONFLICT -> HttpStatus.CONFLICT;
            case PROBLEM -> HttpStatus.BAD_REQUEST;
            case FAILURE -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        return ServerResponse.status(status)
                .body(new ProblemDetails(error.code(), error.description()));
    }

    public static class ProblemDetails {
        private final String code;
        private final String description;

        public ProblemDetails(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }
}