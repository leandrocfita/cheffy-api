package br.com.fiap.cheffy.domain.profile.exception;

import br.com.fiap.cheffy.shared.exception.BusinessException;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

public class ProfileNotFoundException extends BusinessException {

    private final String type;

    public ProfileNotFoundException(ExceptionsKeys message, String type) {
        super(message);
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
