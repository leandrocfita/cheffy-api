package br.com.fiap.cheffy.infrastructure.adapters.out.producer;

import br.com.fiap.cheffy.application.order.dto.OrderCreatedEventPort;
import br.com.fiap.cheffy.domain.order.port.output.OrderEventPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderKafkaProducer implements OrderEventPublisher {

    private final KafkaTemplate<String, OrderCreatedEventPort> kafkaTemplate;
    private final String orderCreatedTopic;

    public OrderKafkaProducer(
            KafkaTemplate<String, OrderCreatedEventPort> kafkaTemplate,
            @Value("${order.events.created-topic:order.created}") String orderCreatedTopic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderCreatedTopic = orderCreatedTopic;
    }

    @Override
    public void publishOrderCreated(OrderCreatedEventPort event) {
        kafkaTemplate.send(orderCreatedTopic, event.orderId().toString(), event);
    }
}
