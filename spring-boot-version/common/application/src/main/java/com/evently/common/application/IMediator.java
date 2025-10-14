package com.evently.common.application;

import com.evently.common.application.messaging.IQuery;
import com.evently.common.domain.Result;

public interface IMediator {
    <TResponse> Result<TResponse> send(ICommand<TResponse> command);

    <TResponse> Result<TResponse> send(IQuery<TResponse> query);
}