package br.com.fiap.cheffy.domain.order.port.input;

import br.com.fiap.cheffy.application.order.ports.in.records.OrderStatusCommandRecord;

public interface UpdateOrderStatusInput {

    void execute(OrderStatusCommandRecord request);
}
