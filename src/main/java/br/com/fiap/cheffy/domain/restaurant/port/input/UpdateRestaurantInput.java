package br.com.fiap.cheffy.domain.restaurant.port.input;

import br.com.fiap.cheffy.application.restaurant.dto.UpdateRestaurantCommandPort;

import java.util.UUID;

public interface UpdateRestaurantInput {
    void execute(UUID restaurantId, UUID userId, UpdateRestaurantCommandPort command);
}