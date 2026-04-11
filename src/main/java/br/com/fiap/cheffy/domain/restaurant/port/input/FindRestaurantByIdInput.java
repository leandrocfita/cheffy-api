package br.com.fiap.cheffy.domain.restaurant.port.input;

import br.com.fiap.cheffy.application.restaurant.dto.RestaurantQueryPort;

import java.util.UUID;

public interface FindRestaurantByIdInput {

    RestaurantQueryPort execute(UUID id);
}
