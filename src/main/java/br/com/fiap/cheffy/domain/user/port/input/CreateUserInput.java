package br.com.fiap.cheffy.domain.user.port.input;

import br.com.fiap.cheffy.application.user.dto.UserCommandPort;

public interface CreateUserInput {

    String execute(UserCommandPort command);
}
