package br.com.fiap.cheffy.infrastructure.messaging.order;

import br.com.fiap.cheffy.application.order.dto.OrderCreatedEventPort;
import br.com.fiap.cheffy.infrastructure.adapters.out.producer.OrderKafkaProducer;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class OrderKafkaProducerTest {

    @Test
    void publishOrderCreatedSendsEventToOrderCreatedTopicWithOrderIdAsKey() {
        CapturingKafkaTemplate kafkaTemplate = new CapturingKafkaTemplate();
        String topic = "order.created";
        OrderKafkaProducer producer = new OrderKafkaProducer(kafkaTemplate, topic);
        UUID orderId = UUID.randomUUID();
        OrderCreatedEventPort event = new OrderCreatedEventPort(
                orderId,
                new BigDecimal("30.00")
        );

        producer.publishOrderCreated(event);

        assertThat(kafkaTemplate.topic).isEqualTo(topic);
        assertThat(kafkaTemplate.key).isEqualTo(orderId.toString());
        assertThat(kafkaTemplate.value).isEqualTo(event);
    }

    private static class CapturingKafkaTemplate extends KafkaTemplate<String, OrderCreatedEventPort> {

        private String topic;
        private String key;
        private OrderCreatedEventPort value;

        private CapturingKafkaTemplate() {
            super(() -> null);
        }

        @Override
        public CompletableFuture<SendResult<String, OrderCreatedEventPort>> send(
                String topic,
                String key,
                OrderCreatedEventPort data
        ) {
            this.topic = topic;
            this.key = key;
            this.value = data;
            return CompletableFuture.completedFuture(null);
        }
    }
}
