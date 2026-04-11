package br.com.fiap.cheffy.domain.user.port.input;

import br.com.fiap.cheffy.application.user.dto.UserQueryPort;
import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;


public interface FindUserByNameInput {
    PageResult<UserQueryPort> execute(String name,PageRequest pageRequest);
}