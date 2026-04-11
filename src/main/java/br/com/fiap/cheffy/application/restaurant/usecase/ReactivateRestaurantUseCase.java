package br.com.fiap.cheffy.application.restaurant.usecase;

import br.com.fiap.cheffy.application.restaurant.service.RestaurantServiceHelper;
import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.restaurant.port.input.ReactivateRestaurantInput;

import java.util.UUID;

public class ReactivateRestaurantUseCase implements ReactivateRestaurantInput {
    private final RestaurantServiceHelper restaurantServiceHelper;

    public ReactivateRestaurantUseCase(
            RestaurantServiceHelper restaurantServiceHelper
    ) {
        this.restaurantServiceHelper = restaurantServiceHelper;
    }

    @Override
    public void execute(UUID id, UUID userId) {
        Restaurant restaurant = restaurantServiceHelper.getRestaurantOrFailValidatingOwnership(id, userId);
        restaurant.reactivate();
        restaurantServiceHelper.saveRestaurant(restaurant);
    }
}
