package com.evently.modules.users.application.users.registeruser;

import com.evently.common.application.ICommandHandler;
import com.evently.common.application.IUnitOfWork;
import com.evently.common.domain.Result;
import com.evently.modules.users.domain.users.IUserRepository;
import com.evently.modules.users.domain.users.User;
import com.evently.modules.users.domain.users.UserErrors;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.util.UUID;

@Component
@Validated
public class RegisterUserCommandHandler implements ICommandHandler<RegisterUserCommand, UUID> {

    private final IUserRepository userRepository;
    private final IUnitOfWork unitOfWork;

    public RegisterUserCommandHandler(IUserRepository userRepository, @Qualifier("usersUnitOfWork") IUnitOfWork unitOfWork) {
        this.userRepository = userRepository;
        this.unitOfWork = unitOfWork;
    }

    @Override
    @Transactional
    public Result<UUID> handle(RegisterUserCommand request) {
        // Check if email is already in use
        if (!userRepository.isEmailUnique(request.email())) {
            return Result.failure(UserErrors.emailAlreadyInUse());
        }

        Result<User> result = User.create(
            request.email(),
            request.firstName(),
            request.lastName(),
            request.identityId());

        if (result.isFailure()) {
            return Result.failure(result.getErrors().get(0));
        }

        User user = result.getValue();
        userRepository.insert(user);

        unitOfWork.saveChanges();

        return Result.success(user.getId());
    }
}