package br.com.fiap.cheffy.presentation.dto;

import jakarta.validation.constraints.AssertTrue;

public record FoodItemAvailabilityDTO(
        Boolean available,
        Boolean deliveryAvailable
) {
    @AssertTrue(message = "Ao menos um campo deve ser informado: available ou deliveryAvailable")
    public boolean isAtLeastOneFieldPresent() {
        return available != null || deliveryAvailable != null;
    }
}
