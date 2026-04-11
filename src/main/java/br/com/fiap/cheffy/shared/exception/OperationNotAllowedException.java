package br.com.fiap.cheffy.shared.exception;


import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

public class OperationNotAllowedException extends BusinessException{
    public OperationNotAllowedException(ExceptionsKeys message) {
        super(message);
    }
}
