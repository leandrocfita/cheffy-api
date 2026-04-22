package br.com.fiap.cheffy.application.order.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemCommandPort(
        UUID foodItemId,
        String name,
        Integer quantity,
        BigDecimal price
) {
}
