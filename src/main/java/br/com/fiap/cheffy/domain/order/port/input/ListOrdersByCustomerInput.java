package br.com.fiap.cheffy.domain.order.port.input;

import br.com.fiap.cheffy.application.order.dto.OrderQueryPort;

import java.util.List;
import java.util.UUID;

public interface ListOrdersByCustomerInput {

    List<OrderQueryPort> execute(UUID customerId);
}
