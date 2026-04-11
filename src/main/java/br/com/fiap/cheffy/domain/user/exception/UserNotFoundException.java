package br.com.fiap.cheffy.domain.user.exception;

import br.com.fiap.cheffy.shared.exception.BusinessException;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

import java.util.UUID;

public class UserNotFoundException extends BusinessException {

    private final UUID id;

    public UserNotFoundException(ExceptionsKeys message, UUID id) {
        super(message);
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
