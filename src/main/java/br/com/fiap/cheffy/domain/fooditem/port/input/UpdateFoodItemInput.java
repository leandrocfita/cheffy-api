package br.com.fiap.cheffy.domain.fooditem.port.input;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemCommandPort;

import java.util.UUID;

public interface UpdateFoodItemInput {

    void update(UUID id, UUID restaurantId, UUID userId, FoodItemCommandPort foodItem);

}
