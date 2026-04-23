package br.com.fiap.cheffy.application.order.dto;

import java.util.UUID;

public record OrderItemCommandPort(
        UUID foodItemId,
        Integer quantity
) {
}
