package com.evently.common.application;

import com.evently.common.application.messaging.IQuery;
import com.evently.common.application.messaging.IQueryHandler;
import com.evently.common.domain.Result;

import java.util.HashMap;
import java.util.Map;

public class Mediator implements IMediator {

    private final Map<Class<?>, ICommandHandler<?, ?>> commandHandlers = new HashMap<>();
    private final Map<Class<?>, IQueryHandler<?, ?>> queryHandlers = new HashMap<>();

    public <TCommand extends ICommand<TResponse>, TResponse> void registerCommandHandler(
            Class<TCommand> commandClass,
            ICommandHandler<TCommand, TResponse> handler) {
        commandHandlers.put(commandClass, handler);
    }

    public <TQuery extends IQuery<TResponse>, TResponse> void registerQueryHandler(
            Class<TQuery> queryClass,
            IQueryHandler<TQuery, TResponse> handler) {
        queryHandlers.put(queryClass, handler);
    }

    // Method for modules to register their handlers
    public void registerHandlers(Object configuration) {
        // This method can be extended by modules to register their handlers
        // For now, we'll keep it simple and let modules call registerCommandHandler/registerQueryHandler directly
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TResponse> Result<TResponse> send(ICommand<TResponse> command) {
        Class<?> commandClass = command.getClass();
        ICommandHandler<ICommand<TResponse>, TResponse> handler =
            (ICommandHandler<ICommand<TResponse>, TResponse>) commandHandlers.get(commandClass);

        if (handler == null) {
            throw new IllegalArgumentException("No handler registered for command: " + commandClass.getName());
        }

        return handler.handle(command);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TResponse> Result<TResponse> send(IQuery<TResponse> query) {
        Class<?> queryClass = query.getClass();
        IQueryHandler<IQuery<TResponse>, TResponse> handler =
            (IQueryHandler<IQuery<TResponse>, TResponse>) queryHandlers.get(queryClass);

        if (handler == null) {
            throw new IllegalArgumentException("No handler registered for query: " + queryClass.getName());
        }

        return handler.handle(query);
    }
}