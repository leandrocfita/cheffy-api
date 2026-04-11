package br.com.fiap.cheffy.domain.profile.exception;

import br.com.fiap.cheffy.shared.exception.BusinessException;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

public class ProfileAlreadyExistException extends BusinessException {

    private final String type;

    public ProfileAlreadyExistException(ExceptionsKeys message, String type) {
        super(message);
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
