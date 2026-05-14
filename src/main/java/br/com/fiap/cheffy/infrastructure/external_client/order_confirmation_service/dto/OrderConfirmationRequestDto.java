package br.com.fiap.cheffy.infrastructure.external_client.order_confirmation_service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderConfirmationRequestDto(
        UUID orderId,
        BigDecimal totalAmount
) {
}
