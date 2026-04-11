package br.com.fiap.cheffy.shared.exception;

import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

public class InvalidDataException extends BusinessException {
    public InvalidDataException(ExceptionsKeys message) {

        super(message);
    }
}
