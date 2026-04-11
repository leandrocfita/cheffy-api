package br.com.fiap.cheffy.application.fooditem.usecase;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemQueryPort;
import br.com.fiap.cheffy.application.fooditem.mapper.FoodItemQueryMapper;
import br.com.fiap.cheffy.application.restaurant.service.RestaurantServiceHelper;
import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.fooditem.port.input.ListFoodItemsByRestaurantInput;
import br.com.fiap.cheffy.domain.fooditem.port.output.FoodItemRepository;
import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantOperationNotAllowedException;

import java.util.List;
import java.util.UUID;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.RESTAURANT_IS_INACTIVE;

public class ListFoodItemsByRestaurantUseCase implements ListFoodItemsByRestaurantInput {

    private final FoodItemRepository foodItemRepository;
    private final RestaurantServiceHelper restaurantServiceHelper;
    private final FoodItemQueryMapper mapper;

    public ListFoodItemsByRestaurantUseCase(
            FoodItemRepository foodItemRepository,
            RestaurantServiceHelper restaurantServiceHelper,
            FoodItemQueryMapper mapper
    ) {
        this.foodItemRepository = foodItemRepository;
        this.restaurantServiceHelper = restaurantServiceHelper;
        this.mapper = mapper;
    }

    @Override
    public PageResult<FoodItemQueryPort> execute(UUID restaurantId, PageRequest pageRequest, boolean includeInactive) {
        Restaurant restaurant = restaurantServiceHelper.getRestaurantOrFail(restaurantId);

        if (!restaurant.isActive()) {
            throw new RestaurantOperationNotAllowedException(RESTAURANT_IS_INACTIVE);
        }

        PageResult<FoodItem> page = includeInactive
                ? foodItemRepository.findAllByRestaurantId(restaurantId, pageRequest)
                : foodItemRepository.findAllActiveByRestaurantId(restaurantId, pageRequest);

        List<FoodItemQueryPort> content = page.content().stream()
                .map(item -> mapper.toQueryPort(item, restaurantId)
                )
                .toList();

        return PageResult.from(page, content);
    }
}
