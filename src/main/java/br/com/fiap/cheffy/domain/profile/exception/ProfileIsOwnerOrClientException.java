package br.com.fiap.cheffy.domain.profile.exception;

import br.com.fiap.cheffy.shared.exception.BusinessException;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

public class ProfileIsOwnerOrClientException extends BusinessException {

    public ProfileIsOwnerOrClientException(ExceptionsKeys message) {
        super(message);
    }
}
