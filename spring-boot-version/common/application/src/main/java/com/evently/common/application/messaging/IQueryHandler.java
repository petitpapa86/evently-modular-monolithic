package com.evently.common.application.messaging;

import com.evently.common.domain.Result;

public interface IQueryHandler<TQuery, TResponse> {
    Result<TResponse> handle(TQuery query);
}