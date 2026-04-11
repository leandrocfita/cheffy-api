package br.com.fiap.cheffy.application.fooditem.usecase;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemAvailabilityCommandPort;
import br.com.fiap.cheffy.application.fooditem.service.FoodItemServiceHelper;
import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.fooditem.exception.FoodItemOperationNotAllowedException;
import br.com.fiap.cheffy.domain.fooditem.port.input.UpdateFoodItemAvailabilityInput;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.*;

public class UpdateFoodItemAvailabilityUseCase implements UpdateFoodItemAvailabilityInput {

    private final FoodItemServiceHelper foodItemServiceHelper;

    public UpdateFoodItemAvailabilityUseCase(FoodItemServiceHelper foodItemServiceHelper) {
        this.foodItemServiceHelper = foodItemServiceHelper;
    }

    @Override
    @Transactional
    public void execute(UUID restaurantId, UUID foodItemId, FoodItemAvailabilityCommandPort command) {
        foodItemServiceHelper.getActiveRestaurantOrFail(restaurantId);

        FoodItem foodItem = foodItemServiceHelper.getFoodItemOrFail(foodItemId, restaurantId);

        if (!foodItem.isActive()) {
            throw new FoodItemOperationNotAllowedException(FOOD_ITEM_INACTIVE_CANNOT_UPDATE_AVAILABILITY);
        }

        boolean resultingAvailable = command.available() != null ? command.available() : foodItem.isAvailable();

        if (Boolean.TRUE.equals(command.deliveryAvailable()) && !resultingAvailable) {
            throw new FoodItemOperationNotAllowedException(FOOD_ITEM_UNAVAILABLE_CANNOT_ENABLE_DELIVERY);
        }

        foodItem.updateAvailability(command.available(), command.deliveryAvailable());
        foodItemServiceHelper.saveFoodItem(foodItem);
    }
}
