package br.com.fiap.cheffy.shared.exception;


import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

public class InvalidOperationException extends BusinessException {

    public InvalidOperationException() {
        super(ExceptionsKeys.INVALID_OPERATION_EXCEPTION);
    }

}