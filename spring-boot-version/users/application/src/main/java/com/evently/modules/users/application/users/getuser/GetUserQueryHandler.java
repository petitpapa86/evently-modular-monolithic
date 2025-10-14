package com.evently.modules.users.application.users.getuser;

import com.evently.common.application.messaging.IQueryHandler;
import com.evently.common.domain.Result;
import com.evently.modules.users.domain.users.IUserRepository;
import com.evently.modules.users.domain.users.User;
import com.evently.modules.users.domain.users.UserErrors;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;

@Component
public class GetUserQueryHandler implements IQueryHandler<GetUserQuery, UserResponse> {

    private final IUserRepository userRepository;

    public GetUserQueryHandler(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Result<UserResponse> handle(GetUserQuery request) {
        Optional<User> userOpt = userRepository.get(request.userId());
        if (userOpt.isEmpty()) {
            return Result.failure(UserErrors.notFound(request.userId()));
        }

        User user = userOpt.get();
        UserResponse response = new UserResponse(
            user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName());

        return Result.success(response);
    }
}