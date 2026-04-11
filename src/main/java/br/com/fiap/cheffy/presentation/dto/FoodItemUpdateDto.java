package br.com.fiap.cheffy.presentation.dto;

import br.com.fiap.cheffy.presentation.interfaces.FoodItemRequest;
import br.com.fiap.cheffy.presentation.validation.NotBlankIfPresent;

import java.math.BigDecimal;

public record FoodItemUpdateDto(
        @NotBlankIfPresent
        String name,
        @NotBlankIfPresent
        String description,
        BigDecimal price,
        @NotBlankIfPresent
        String photoKey,
        boolean deliveryAvailable,
        boolean available,
        boolean active
) implements FoodItemRequest {
}
