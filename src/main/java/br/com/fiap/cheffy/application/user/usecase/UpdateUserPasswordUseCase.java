package br.com.fiap.cheffy.application.user.usecase;

import br.com.fiap.cheffy.application.user.dto.UserCommandPort;
import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.domain.user.exception.UserNotFoundException;
import br.com.fiap.cheffy.domain.user.port.input.PasswordEncoderPort;
import br.com.fiap.cheffy.domain.user.port.input.UpdateUserPasswordInput;
import br.com.fiap.cheffy.domain.user.port.output.UserRepository;

import java.util.UUID;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.USER_NOT_FOUND_EXCEPTION;

public class UpdateUserPasswordUseCase implements UpdateUserPasswordInput {

    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoderPort;

    public UpdateUserPasswordUseCase(
            UserRepository userRepository,
            PasswordEncoderPort passwordEncoderPort
    ) {
        this.userRepository = userRepository;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    public void execute(UserCommandPort command, UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_EXCEPTION, id));

        updatePassword(user, command.password());

        userRepository.save(user);
    }

    private void updatePassword(User user, String rawPassword) {
        String encodedPassword = passwordEncoderPort.encode(rawPassword);

        User.validatePassword(rawPassword);
        user.setPassword(encodedPassword);
    }
}
