package br.com.fiap.cheffy.domain.order.port.input;

import br.com.fiap.cheffy.application.order.dto.OrderQueryPort;
import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;

import java.util.UUID;

public interface ListOrdersByCustomerInput {
    PageResult<OrderQueryPort> execute(UUID customerId, PageRequest pageRequest);
}
