package br.com.fiap.cheffy.domain.fooditem.exception;

import br.com.fiap.cheffy.shared.exception.OperationNotAllowedException;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

public class FoodItemOperationNotAllowedException extends OperationNotAllowedException {
    public FoodItemOperationNotAllowedException(ExceptionsKeys message) {
        super(message);
    }
}
