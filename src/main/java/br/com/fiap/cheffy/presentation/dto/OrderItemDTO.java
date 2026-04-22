package br.com.fiap.cheffy.presentation.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemDTO(
        @NotNull
        UUID foodItemId,

        @NotBlank
        String name,

        @NotNull
        @Positive
        Integer quantity,

        @NotNull
        @DecimalMin("0.1")
        BigDecimal price
) {
}
