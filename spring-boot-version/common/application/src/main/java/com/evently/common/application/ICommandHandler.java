package com.evently.common.application;

import com.evently.common.domain.Result;

public interface ICommandHandler<TCommand extends ICommand<TResponse>, TResponse> {
    Result<TResponse> handle(TCommand command);
}