package br.com.fiap.cheffy.application.fooditem.mapper;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemQueryPort;
import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;

import java.util.UUID;

public class FoodItemQueryMapper {

    public FoodItemQueryMapper() {
    }
    public FoodItemQueryPort toQueryPort(FoodItem foodItem, UUID restaurantId) {
        return new FoodItemQueryPort(
                foodItem.getId(),
                foodItem.getName(),
                foodItem.getDescription(),
                foodItem.getPrice().value(),
                foodItem.getPhotoKey(),
                restaurantId,
                foodItem.isDeliveryAvailable(),
                foodItem.isAvailable(),
                foodItem.isActive()
        );
    }
}
