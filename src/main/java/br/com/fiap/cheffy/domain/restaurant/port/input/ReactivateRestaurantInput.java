package br.com.fiap.cheffy.domain.restaurant.port.input;

import java.util.UUID;

public interface ReactivateRestaurantInput {

    void execute(UUID id, UUID userId);
}
