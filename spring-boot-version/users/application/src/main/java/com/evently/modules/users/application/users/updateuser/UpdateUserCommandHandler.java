package com.evently.modules.users.application.users.updateuser;

import com.evently.common.application.ICommandHandler;
import com.evently.common.application.IUnitOfWork;
import com.evently.common.domain.Result;
import com.evently.modules.users.domain.users.IUserRepository;
import com.evently.modules.users.domain.users.User;
import com.evently.modules.users.domain.users.UserErrors;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Component
public class UpdateUserCommandHandler implements ICommandHandler<UpdateUserCommand, Void> {

    private final IUserRepository userRepository;
    private final IUnitOfWork unitOfWork;

    public UpdateUserCommandHandler(IUserRepository userRepository, @Qualifier("usersUnitOfWork") IUnitOfWork unitOfWork) {
        this.userRepository = userRepository;
        this.unitOfWork = unitOfWork;
    }

    @Override
    @Transactional
    public Result<Void> handle(UpdateUserCommand request) {
        Optional<User> userOpt = userRepository.get(request.userId());
        if (userOpt.isEmpty()) {
            return Result.failure(UserErrors.notFound(request.userId()));
        }

        User user = userOpt.get();
        user.update(request.firstName(), request.lastName());

        unitOfWork.saveChanges();

        return Result.success(null);
    }
}