package br.com.fiap.cheffy.domain.user.port.output;

import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.user.entity.User;


import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    boolean existsByEmailOrLogin(String email, String login);

    Optional<User> findByLogin(String login);

    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);

    PageResult<User> findAll(PageRequest pageRequest);

    PageResult<User> findByName(String name, PageRequest pageRequest);
}
