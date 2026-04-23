package br.com.fiap.cheffy.presentation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record OrderItemDTO(
        @NotNull
        UUID foodItemId,

        @NotNull
        @Positive
        Integer quantity
) {
}
