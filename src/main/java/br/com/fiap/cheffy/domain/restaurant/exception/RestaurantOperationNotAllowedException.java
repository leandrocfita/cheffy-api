package br.com.fiap.cheffy.domain.restaurant.exception;

import br.com.fiap.cheffy.shared.exception.BusinessException;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

public class RestaurantOperationNotAllowedException extends BusinessException {
    public RestaurantOperationNotAllowedException(ExceptionsKeys message) {
        super(message);
    }
}
