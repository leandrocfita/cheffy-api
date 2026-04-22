package br.com.fiap.cheffy.application.order.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderCreatedEventItemPort(
        UUID foodItemId,
        String name,
        Integer quantity,
        BigDecimal price
) {
}
