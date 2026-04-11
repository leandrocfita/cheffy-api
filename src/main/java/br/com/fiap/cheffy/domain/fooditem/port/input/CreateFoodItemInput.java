package br.com.fiap.cheffy.domain.fooditem.port.input;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemCommandPort;
import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;

public interface CreateFoodItemInput {

    FoodItem execute(FoodItemCommandPort foodItemCommandPort);
}
