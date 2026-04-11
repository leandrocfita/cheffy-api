package br.com.fiap.cheffy.domain.user.exception;

import br.com.fiap.cheffy.shared.exception.BusinessException;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

public class UserOperationNotAllowedException extends BusinessException {
    public UserOperationNotAllowedException(ExceptionsKeys message){
        super(message);
    }
}
