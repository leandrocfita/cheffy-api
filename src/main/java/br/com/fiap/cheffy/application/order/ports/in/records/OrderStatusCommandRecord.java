package br.com.fiap.cheffy.application.order.ports.in.records;

import java.util.UUID;

public record OrderStatusCommandRecord(
        UUID orderId,
        String status
) {
}
