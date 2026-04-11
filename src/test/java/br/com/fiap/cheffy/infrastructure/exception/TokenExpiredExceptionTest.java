package br.com.fiap.cheffy.infrastructure.exception;

import org.junit.jupiter.api.Test;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.TOKEN_EXPIRED_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;

class TokenExpiredExceptionTest {

    @Test
    void createTokenExpiredException() {
        TokenExpiredException ex = new TokenExpiredException(TOKEN_EXPIRED_EXCEPTION);
        assertThat(ex.getMessage()).contains("TOKEN_EXPIRED_EXCEPTION");
    }
}
