package br.com.fiap.cheffy.application.order.dto;

import br.com.fiap.cheffy.domain.order.entity.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderQueryPort(
        UUID id,
        UUID customerId,
        UUID restaurantId,
        BigDecimal totalAmount,
        OrderStatus status,
        List<OrderItemQueryPort> items
) {
}
