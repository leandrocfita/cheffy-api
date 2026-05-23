package br.com.fiap.cheffy.domain.order.port.input;

import java.util.UUID;

public interface UpdateOrderStatusInput {

    void execute(UUID orderId, String status);
}
