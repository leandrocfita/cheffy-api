package br.com.fiap.cheffy.application.order.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderCreatedEventPort(
        UUID orderId,
        BigDecimal totalAmount
) {
}
