package br.com.fiap.cheffy.application.restaurant.dto;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemQueryPort;
import br.com.fiap.cheffy.application.user.dto.AddressQueryPort;

import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

public record RestaurantQueryPort(
        UUID id,
        String name,
        String culinary,
        LocalTime openingTime,
        LocalTime closingTime,
        boolean open24hours,
        AddressQueryPort address,
        UUID ownerId,
        Set<FoodItemQueryPort> menu
) {
}
