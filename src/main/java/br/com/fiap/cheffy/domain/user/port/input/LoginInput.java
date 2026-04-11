package br.com.fiap.cheffy.domain.user.port.input;

import br.com.fiap.cheffy.application.user.dto.LoginResultPort;

public interface LoginInput {

    LoginResultPort execute(String login, String password);
}
