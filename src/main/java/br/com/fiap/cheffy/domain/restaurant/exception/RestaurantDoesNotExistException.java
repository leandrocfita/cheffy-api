package br.com.fiap.cheffy.domain.restaurant.exception;

import br.com.fiap.cheffy.shared.exception.BusinessException;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

public class RestaurantDoesNotExistException extends BusinessException {

    private final String type;

    public RestaurantDoesNotExistException(ExceptionsKeys message, String type) {
        super(message);
        this.type = type;
    }

    public String getType(){ return  this.type; }
}
