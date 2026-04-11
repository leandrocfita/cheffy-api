package br.com.fiap.cheffy.domain.user.exception;

import br.com.fiap.cheffy.shared.exception.BusinessException;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

public class InvalidPostalCodeException extends BusinessException {

    int minPostalCodeLength;

    public InvalidPostalCodeException(final ExceptionsKeys message, final int minPostalCodeLength) {
        super(message);
        this.minPostalCodeLength = minPostalCodeLength;
    }

    public int getMinPostalCodeLength() {
        return minPostalCodeLength;
    }
}
