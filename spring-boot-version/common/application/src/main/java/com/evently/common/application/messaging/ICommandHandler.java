package com.evently.common.application.messaging;

import com.evently.common.domain.Result;

public interface ICommandHandler<T extends ICommand> {
    Result handle(T command);
}