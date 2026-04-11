package br.com.fiap.cheffy.application.fooditem.usecase;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemQueryPort;
import br.com.fiap.cheffy.application.fooditem.mapper.FoodItemQueryMapper;
import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.fooditem.exception.FoodItemNotFoundException;
import br.com.fiap.cheffy.domain.fooditem.port.input.FindFoodItemByIdInput;
import br.com.fiap.cheffy.domain.fooditem.port.output.FoodItemRepository;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantInactiveException;

import java.util.UUID;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.FOOD_ITEM_NOT_FOUND_EXCEPTION;
import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.RESTAURANT_IS_INACTIVE;

public class FindFoodItemByIdUseCase implements FindFoodItemByIdInput {

    private final FoodItemRepository foodItemRepository;
    private final FoodItemQueryMapper mapper;

    public FindFoodItemByIdUseCase(
            FoodItemRepository foodItemRepository,
            FoodItemQueryMapper mapper
    ) {
        this.foodItemRepository = foodItemRepository;
        this.mapper = mapper;
    }

    @Override
    public FoodItemQueryPort execute(UUID restaurantId, UUID foodItemId) {
        FoodItem foodItem = foodItemRepository.findByIdAndRestaurantId(foodItemId, restaurantId)
                .orElseThrow(() -> new FoodItemNotFoundException(FOOD_ITEM_NOT_FOUND_EXCEPTION, foodItemId));
        if (!foodItem.getRestaurant().isActive()) {
            throw new RestaurantInactiveException(RESTAURANT_IS_INACTIVE, restaurantId);
        }
        return mapper.toQueryPort(foodItem, restaurantId);
    }
}