package br.com.fiap.cheffy.application.order.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderConfirmationCommandPort(
        UUID orderId,
        BigDecimal totalAmount,
        String authorizationHeader
) {
}
