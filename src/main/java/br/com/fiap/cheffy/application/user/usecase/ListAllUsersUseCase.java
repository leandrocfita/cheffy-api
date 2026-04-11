package br.com.fiap.cheffy.application.user.usecase;

import br.com.fiap.cheffy.application.user.dto.UserQueryPort;
import br.com.fiap.cheffy.application.user.mapper.UserQueryMapper;
import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.domain.user.port.input.ListAllUsersInput;
import br.com.fiap.cheffy.domain.user.port.output.UserRepository;

import java.util.List;


public class ListAllUsersUseCase implements ListAllUsersInput {

    private final UserRepository userRepository;
    private final UserQueryMapper mapper;

    public ListAllUsersUseCase(UserRepository userRepository, UserQueryMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public PageResult<UserQueryPort> execute(PageRequest pageRequest) {

        PageResult<User> userPage = userRepository.findAll(pageRequest);

        List<UserQueryPort> mappedContent = userPage.content().stream()
                .map(mapper::toQuery)
                .toList();

        return PageResult.from(userPage, mappedContent);
    }
}