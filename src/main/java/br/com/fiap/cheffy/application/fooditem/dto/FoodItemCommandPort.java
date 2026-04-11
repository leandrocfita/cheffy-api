package br.com.fiap.cheffy.application.fooditem.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record FoodItemCommandPort(
        String name,
        String description,
        BigDecimal price,
        String photoKey,
        UUID restaurantId,
        Boolean deliveryAvailable,
        Boolean available,
        boolean active
) {
}
