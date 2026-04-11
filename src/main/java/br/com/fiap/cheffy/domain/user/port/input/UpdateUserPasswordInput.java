package br.com.fiap.cheffy.domain.user.port.input;

import br.com.fiap.cheffy.application.user.dto.UserCommandPort;

import java.util.UUID;

public interface UpdateUserPasswordInput {

    void execute(UserCommandPort command, UUID id);
}
