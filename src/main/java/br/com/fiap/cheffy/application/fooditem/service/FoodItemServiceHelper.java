package br.com.fiap.cheffy.application.fooditem.service;

import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.fooditem.exception.FoodItemNotFoundException;
import br.com.fiap.cheffy.domain.fooditem.exception.FoodItemUnavailableForOrderException;
import br.com.fiap.cheffy.domain.fooditem.port.output.FoodItemRepository;
import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantNotFoundException;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantOperationNotAllowedException;
import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;

import java.util.UUID;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.*;

public class FoodItemServiceHelper {

    private final FoodItemRepository foodItemRepository;
    private final RestaurantRepository restaurantRepository;

    public FoodItemServiceHelper(FoodItemRepository foodItemRepository, RestaurantRepository restaurantRepository) {
        this.foodItemRepository = foodItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public Restaurant getActiveRestaurantOrFail(UUID restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(RESTAURANT_NOT_FOUND_EXCEPTION, restaurantId));
        if (!restaurant.isActive()) {
            throw new RestaurantOperationNotAllowedException(RESTAURANT_IS_INACTIVE_CANNOT_UPDATE);
        }
        return restaurant;
    }

    public FoodItem getFoodItemOrFail(UUID foodItemId, UUID restaurantId) {
        return foodItemRepository.findByIdAndRestaurantId(foodItemId, restaurantId)
                .orElseThrow(() -> new FoodItemNotFoundException(FOOD_ITEM_NOT_FOUND, foodItemId));
    }

    public FoodItem getAvailableFoodItemOrFail(UUID foodItemId, UUID restaurantId) {
        FoodItem foodItem = getFoodItemOrFail(foodItemId, restaurantId);

        if (!foodItem.isActive() || !foodItem.isAvailable()) {
            throw new FoodItemUnavailableForOrderException(FOOD_ITEM_UNAVAILABLE_FOR_ORDER, foodItemId);
        }

        return foodItem;
    }

    public void saveFoodItem(FoodItem foodItem) {
        foodItemRepository.save(foodItem);
    }
}
