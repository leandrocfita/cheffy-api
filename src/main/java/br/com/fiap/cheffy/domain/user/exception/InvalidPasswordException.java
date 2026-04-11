package br.com.fiap.cheffy.domain.user.exception;

import br.com.fiap.cheffy.shared.exception.BusinessException;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

public class InvalidPasswordException extends BusinessException {

    int minPasswordLength;

    public InvalidPasswordException(final ExceptionsKeys message, final int minPasswordLength) {
        super(message);
        this.minPasswordLength = minPasswordLength;
    }

    public int getMinPasswordLength() {
        return minPasswordLength;
    }
}
