package br.com.fiap.cheffy.infrastructure.kafka.dto;

import java.util.UUID;

public record OrderStatusUpdatedEvent(
        UUID orderId,
        String status) {
}
