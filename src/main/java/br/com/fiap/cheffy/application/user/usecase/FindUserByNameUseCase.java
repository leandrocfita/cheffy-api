package br.com.fiap.cheffy.application.user.usecase;

import br.com.fiap.cheffy.application.user.dto.UserQueryPort;
import br.com.fiap.cheffy.application.user.mapper.UserQueryMapper;
import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.domain.user.port.input.FindUserByNameInput;
import br.com.fiap.cheffy.domain.user.port.output.UserRepository;

public class FindUserByNameUseCase implements FindUserByNameInput {

    private final UserRepository userRepository;
    private final UserQueryMapper mapper;

    public FindUserByNameUseCase(UserRepository userRepository, UserQueryMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public PageResult<UserQueryPort> execute(String name, PageRequest pageRequest) {

        PageResult<User> userPage = userRepository.findByName(name, pageRequest);

        var mappedContent = userPage.content().stream()
                .map(mapper::toQuery)
                .toList();

        return PageResult.from(userPage, mappedContent);
    }
}