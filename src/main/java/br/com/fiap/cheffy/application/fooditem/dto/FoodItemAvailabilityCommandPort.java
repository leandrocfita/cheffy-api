package br.com.fiap.cheffy.application.fooditem.dto;

public record FoodItemAvailabilityCommandPort(
        Boolean available,
        Boolean deliveryAvailable
) {
}
