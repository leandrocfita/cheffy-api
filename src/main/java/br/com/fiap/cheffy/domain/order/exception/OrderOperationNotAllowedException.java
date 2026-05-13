package br.com.fiap.cheffy.domain.order.exception;

import br.com.fiap.cheffy.shared.exception.OperationNotAllowedException;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

public class OrderOperationNotAllowedException extends OperationNotAllowedException {

    public OrderOperationNotAllowedException(ExceptionsKeys message) {
        super(message);
    }
}
