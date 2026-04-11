package br.com.fiap.cheffy.domain.fooditem.exception;

import br.com.fiap.cheffy.shared.exception.BusinessException;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

public class FoodItemAlreadyExistInRestaurant extends BusinessException {

    private final String type;
    public FoodItemAlreadyExistInRestaurant(ExceptionsKeys message, String type)
    {
        super(message);
        this.type = type;
    }

    public String getType() {return  this.type;}
}
