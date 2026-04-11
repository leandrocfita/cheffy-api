package br.com.fiap.cheffy.application.user.usecase;

import br.com.fiap.cheffy.application.user.dto.UserCommandPort;
import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.domain.user.exception.UserNotFoundException;
import br.com.fiap.cheffy.domain.user.port.input.UpdateUserInput;
import br.com.fiap.cheffy.domain.user.port.output.UserRepository;
import br.com.fiap.cheffy.shared.exception.InvalidOperationException;

import java.util.Objects;
import java.util.UUID;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.USER_NOT_FOUND_EXCEPTION;

public class UpdateUserUseCase implements UpdateUserInput {

    private final UserRepository userRepository;

    public UpdateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(UUID id, UserCommandPort command) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_EXCEPTION, id));

//        validateUniqueParameters(command, id);

        user.patch(
                command.name(),
                command.email(),
                command.login()
        );

        userRepository.save(user);
    }

//    private void validateUniqueParameters(UserCommandPort command, UUID id) {
//        if (command.email() != null && existsUserWithEmail(command.email(), id)) {
//            throw new InvalidOperationException();
//        }
//        if (command.login() != null && existsUserWithLogin(command.login(), id)) {
//            throw new InvalidOperationException();
//        }
//    }

//    private boolean existsUserWithEmail(String email, UUID id) {
//        return userRepository.findByEmail(email)
//                .filter(user -> !Objects.equals(user.getId(), id))
//                .isPresent();
//    }
//
//    private boolean existsUserWithLogin(String login, UUID id) {
//        return userRepository.findByLogin(login)
//                .filter(user -> !Objects.equals(user.getId(), id))
//                .isPresent();
//    }
}
