package br.com.fiap.cheffy.presentation.exception;

import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

public class ApiInternalServerErrorException extends ApiException {
    public ApiInternalServerErrorException(ExceptionsKeys message) {
        super(message);
    }

}
