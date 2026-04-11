package br.com.fiap.cheffy.shared.exception;


import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

public class BusinessException extends RuntimeException{
    public BusinessException(ExceptionsKeys message){

        super(String.valueOf(message));
    }
}
