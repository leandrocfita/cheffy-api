package br.com.fiap.cheffy.shared.exception;


import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

public class RegisterFailedException extends BusinessException{
    public RegisterFailedException(ExceptionsKeys message) {
        super(message);
    }
}
