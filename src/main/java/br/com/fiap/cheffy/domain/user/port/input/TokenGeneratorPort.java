package br.com.fiap.cheffy.domain.user.port.input;

import br.com.fiap.cheffy.domain.user.entity.AuthenticatedUser;

public interface TokenGeneratorPort {

    String generate(AuthenticatedUser user);
}
