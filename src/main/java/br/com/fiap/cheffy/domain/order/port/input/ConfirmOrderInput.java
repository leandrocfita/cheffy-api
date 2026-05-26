package br.com.fiap.cheffy.domain.order.port.input;

import br.com.fiap.cheffy.application.order.dto.OrderQueryPort;

import java.util.UUID;

public interface ConfirmOrderInput {

    OrderQueryPort execute(UUID orderId, UUID customerId);
}
