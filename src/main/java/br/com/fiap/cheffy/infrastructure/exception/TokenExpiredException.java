package br.com.fiap.cheffy.infrastructure.exception;

import br.com.fiap.cheffy.shared.exception.BusinessException;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

public class TokenExpiredException extends BusinessException {

    public TokenExpiredException(ExceptionsKeys message) {
        super(message);
    }
}

