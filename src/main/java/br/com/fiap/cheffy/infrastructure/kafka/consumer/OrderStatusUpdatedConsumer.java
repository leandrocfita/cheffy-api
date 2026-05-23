package br.com.fiap.cheffy.infrastructure.kafka.consumer;

import br.com.fiap.cheffy.application.order.usecase.UpdateOrderStatusUseCase;
import br.com.fiap.cheffy.infrastructure.kafka.dto.OrderStatusUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderStatusUpdatedConsumer {

    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;

    @KafkaListener(
            topics = "${order.events.status-changes-topic:order.status-changes}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumer(OrderStatusUpdatedEvent event, Acknowledgment ack) {
        try {
            log.info("Message received: [orderId={}, status={}]", event.orderId(), event.status());
            updateOrderStatusUseCase.execute(event.orderId(), event.status());
            ack.acknowledge();
            log.info("Message processed successfully: [orderId={}, status={}]", event.orderId(), event.status());
        } catch (Exception e) {
            log.error("Error processing message: [orderId={}, status={}], error: {}", event.orderId(), event.status(), e.getMessage(), e);
            ack.nack(Duration.ofDays(1000));
        }
    }
}
