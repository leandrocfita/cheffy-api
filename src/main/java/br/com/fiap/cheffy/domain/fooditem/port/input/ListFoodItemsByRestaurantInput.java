package br.com.fiap.cheffy.domain.fooditem.port.input;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemQueryPort;
import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;

import java.util.UUID;

public interface ListFoodItemsByRestaurantInput {

    PageResult<FoodItemQueryPort> execute(UUID restaurantId, PageRequest pageRequest, boolean includeInactive);
}
