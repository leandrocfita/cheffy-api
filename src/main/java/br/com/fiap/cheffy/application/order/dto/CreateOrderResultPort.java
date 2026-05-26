package br.com.fiap.cheffy.application.order.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateOrderResultPort(
        UUID orderId,
        BigDecimal totalAmount
) {
}
