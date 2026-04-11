package br.com.fiap.cheffy.domain.fooditem.port.input;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemAvailabilityCommandPort;

import java.util.UUID;

public interface UpdateFoodItemAvailabilityInput {
    void execute(UUID restaurantId, UUID foodItemId, FoodItemAvailabilityCommandPort command);
}
