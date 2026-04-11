package br.com.fiap.cheffy.domain.user.port.input;

import java.util.UUID;

public interface DeactivateUserInput {

    void execute(UUID id);
}
