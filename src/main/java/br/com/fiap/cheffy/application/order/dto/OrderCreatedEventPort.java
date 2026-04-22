package br.com.fiap.cheffy.application.order.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderCreatedEventPort(
        UUID orderId,
        UUID customerId,
        UUID restaurantId,
        BigDecimal totalAmount,
        List<OrderCreatedEventItemPort> items
) {
}
