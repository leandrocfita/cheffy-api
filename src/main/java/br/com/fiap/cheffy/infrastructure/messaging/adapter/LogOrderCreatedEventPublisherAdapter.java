package br.com.fiap.cheffy.infrastructure.messaging.adapter;

import br.com.fiap.cheffy.application.order.dto.OrderCreatedEventPort;
import br.com.fiap.cheffy.domain.order.port.output.OrderCreatedEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogOrderCreatedEventPublisherAdapter implements OrderCreatedEventPublisher {

    @Override
    public void publish(OrderCreatedEventPort event) {
        log.info("OrderCreatedEventPublisher.publish - topic [pedido.criado] - orderId [{}]", event.orderId());
    }
}
