package br.com.fiap.cheffy.domain.fooditem.port.input;

import java.util.UUID;

public interface DeactivateFoodItemInput {
    void execute(UUID restaurantId, UUID foodItemId);
}
