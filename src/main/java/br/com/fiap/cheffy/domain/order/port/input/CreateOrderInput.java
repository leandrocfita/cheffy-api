package br.com.fiap.cheffy.domain.order.port.input;

import br.com.fiap.cheffy.application.order.dto.OrderCommandPort;

import java.util.UUID;

public interface CreateOrderInput {

    String execute(OrderCommandPort command, UUID customerId);
}
