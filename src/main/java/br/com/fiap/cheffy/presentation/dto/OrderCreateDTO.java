package br.com.fiap.cheffy.presentation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record OrderCreateDTO(
        @NotNull
        UUID restaurantId,

        @Valid
        @NotEmpty
        List<OrderItemDTO> items
) {
}
