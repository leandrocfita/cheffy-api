package br.com.fiap.cheffy.application.restaurant.usecase;

import br.com.fiap.cheffy.application.restaurant.service.RestaurantServiceHelper;
import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantOperationNotAllowedException;
import br.com.fiap.cheffy.domain.restaurant.port.input.DeactivateRestaurantInput;

import java.util.UUID;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.*;

public class DeactivateRestaurantUseCase implements DeactivateRestaurantInput {
    private final RestaurantServiceHelper restaurantServiceHelper;

    public DeactivateRestaurantUseCase(
            RestaurantServiceHelper restaurantServiceHelper
    ) {
        this.restaurantServiceHelper = restaurantServiceHelper;
    }

    @Override
    public void execute(UUID id, UUID userId) {
        Restaurant restaurant = restaurantServiceHelper.getRestaurantOrFailValidatingOwnership(id, userId);
        restaurant.deactivate();
        restaurantServiceHelper.saveRestaurant(restaurant);
    }
}
