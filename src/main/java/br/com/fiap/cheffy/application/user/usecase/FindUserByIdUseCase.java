package br.com.fiap.cheffy.application.user.usecase;

import br.com.fiap.cheffy.application.user.dto.UserQueryPort;
import br.com.fiap.cheffy.application.user.mapper.UserQueryMapper;
import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.domain.user.exception.UserNotFoundException;
import br.com.fiap.cheffy.domain.user.port.input.FindUserByIdInput;
import br.com.fiap.cheffy.domain.user.port.output.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.USER_NOT_FOUND_EXCEPTION;

public class FindUserByIdUseCase implements FindUserByIdInput {

    private final UserRepository userRepository;
    private final UserQueryMapper mapper;

    public FindUserByIdUseCase(UserRepository userRepository, UserQueryMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public UserQueryPort execute(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_EXCEPTION, id));
        
        return mapper.toQuery(user);
    }
}