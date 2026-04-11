package br.com.fiap.cheffy.application.user.usecase;

import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.domain.user.exception.UserNotFoundException;
import br.com.fiap.cheffy.domain.user.exception.UserOperationNotAllowedException;
import br.com.fiap.cheffy.domain.user.port.input.ReactivateUserInput;
import br.com.fiap.cheffy.domain.user.port.output.UserRepository;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.USER_IS_ALREADY_ACTIVE;
import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.USER_NOT_FOUND_EXCEPTION;

public class ReactivateUserUseCase implements ReactivateUserInput {
    private final UserRepository userRepository;

    public ReactivateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void execute(java.util.UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_EXCEPTION, id));

        if (user.isActive()) {
            throw new UserOperationNotAllowedException(USER_IS_ALREADY_ACTIVE);
        }

        user.activate();
        userRepository.save(user);
    }
}
