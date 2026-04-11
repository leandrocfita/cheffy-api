package br.com.fiap.cheffy.application.user.service;

import br.com.fiap.cheffy.application.user.dto.UserQueryPort;
import br.com.fiap.cheffy.application.user.mapper.UserQueryMapper;
import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.domain.user.exception.UserNotFoundException;
import br.com.fiap.cheffy.domain.user.port.output.UserRepository;

import java.util.UUID;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.USER_NOT_FOUND_EXCEPTION;

public class UserServiceHelper {

    private final UserRepository userRepository;
    private final UserQueryMapper mapper;

    public UserServiceHelper(
            UserRepository userRepository,
            UserQueryMapper mapper
    ){
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public User getUserOrFail(UUID id){
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_EXCEPTION, id));
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public UserQueryPort userToQueryPort(User user){
        return mapper.toQuery(user);
    }

}
