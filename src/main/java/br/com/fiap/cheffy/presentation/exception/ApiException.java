package br.com.fiap.cheffy.presentation.exception;


import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

public class ApiException extends RuntimeException{
    public ApiException(ExceptionsKeys message) {
        super(String.valueOf(message));
    }
}
