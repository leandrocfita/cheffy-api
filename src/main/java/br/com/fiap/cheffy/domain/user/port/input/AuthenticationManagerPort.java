package br.com.fiap.cheffy.domain.user.port.input;

import br.com.fiap.cheffy.domain.user.entity.AuthenticatedUser;

public interface AuthenticationManagerPort {

    AuthenticatedUser authenticate(String username, String password);


}
