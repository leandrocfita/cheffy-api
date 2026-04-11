package br.com.fiap.cheffy.domain.fooditem.port.output;

import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FoodItemRepository {

    FoodItem save(FoodItem foodItem);
    Optional<FoodItem> findById(UUID foodItemId);
    Optional<FoodItem> findByIdAndRestaurantId(UUID foodItemId, UUID restaurantId);
    List<FoodItem> findAllByRestaurantId(UUID restaurantId);
    PageResult<FoodItem> findAllByRestaurantId(UUID restaurantId, PageRequest pageRequest);
    PageResult<FoodItem> findAllActiveByRestaurantId(UUID restaurantId, PageRequest pageRequest);
    boolean existsInRestaurantById(UUID restaurantId, UUID foodItemId);
    boolean existsByNameIgnoreCaseAndRestaurantId(String foodName, UUID restaurantId);
}
