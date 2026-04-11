package br.com.fiap.cheffy.application.fooditem.usecase;

import br.com.fiap.cheffy.application.fooditem.service.FoodItemServiceHelper;
import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.fooditem.exception.FoodItemOperationNotAllowedException;
import br.com.fiap.cheffy.domain.fooditem.port.input.ReactivateFoodItemInput;

import java.util.UUID;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.*;

public class ReactivateFoodItemUseCase implements ReactivateFoodItemInput {

    private final FoodItemServiceHelper foodItemServiceHelper;

    public ReactivateFoodItemUseCase(FoodItemServiceHelper foodItemServiceHelper) {
        this.foodItemServiceHelper = foodItemServiceHelper;
    }

    @Override
    public void execute(UUID restaurantId, UUID foodItemId) {
        foodItemServiceHelper.getActiveRestaurantOrFail(restaurantId);

        FoodItem foodItem = foodItemServiceHelper.getFoodItemOrFail(foodItemId, restaurantId);

        if (foodItem.isActive()) {
            throw new FoodItemOperationNotAllowedException(FOOD_ITEM_IS_ALREADY_ACTIVE);
        }

        foodItem.reactivate();
        foodItemServiceHelper.saveFoodItem(foodItem);
    }
}
