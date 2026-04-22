package br.com.fiap.cheffy.domain.order.port.output;

import br.com.fiap.cheffy.application.order.dto.OrderCreatedEventPort;

public interface OrderCreatedEventPublisher {

    void publish(OrderCreatedEventPort event);
}
