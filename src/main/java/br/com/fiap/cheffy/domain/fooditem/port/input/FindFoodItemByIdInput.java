package br.com.fiap.cheffy.domain.fooditem.port.input;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemQueryPort;

import java.util.UUID;

public interface FindFoodItemByIdInput {

    FoodItemQueryPort execute(UUID restaurantId, UUID foodItemId);
}