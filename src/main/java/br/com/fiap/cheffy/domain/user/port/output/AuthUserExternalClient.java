package br.com.fiap.cheffy.domain.user.port.output;

import br.com.fiap.cheffy.application.user.dto.AuthUserCommandPort;

public interface AuthUserExternalClient {

    String createUser(AuthUserCommandPort command);
}
