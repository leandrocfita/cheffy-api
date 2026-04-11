package br.com.fiap.cheffy.domain.user.port.input;

public interface PasswordEncoderPort {

    String encode(CharSequence rawPassword);
}
