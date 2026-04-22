package br.com.fiap.cheffy.domain.order.exception;

import br.com.fiap.cheffy.shared.exception.BusinessException;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

import java.util.UUID;

public class OrderNotFoundException extends BusinessException {

    private final UUID id;

    public OrderNotFoundException(ExceptionsKeys message, UUID id) {
        super(message);
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
