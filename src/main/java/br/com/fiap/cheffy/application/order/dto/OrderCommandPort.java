package br.com.fiap.cheffy.application.order.dto;

import java.util.List;
import java.util.UUID;

public record OrderCommandPort(
        UUID restaurantId,
        List<OrderItemCommandPort> items
) {
}
