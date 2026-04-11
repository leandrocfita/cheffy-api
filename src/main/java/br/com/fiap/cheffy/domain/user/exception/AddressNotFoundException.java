package br.com.fiap.cheffy.domain.user.exception;

import br.com.fiap.cheffy.shared.exception.BusinessException;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

public class AddressNotFoundException extends BusinessException {

    private final Long id;

    public AddressNotFoundException(ExceptionsKeys message, Long id) {
        super(message);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
