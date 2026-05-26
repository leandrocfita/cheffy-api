package br.com.fiap.cheffy.infrastructure.adapters.in.consumer;

import br.com.fiap.cheffy.application.order.usecase.UpdateOrderStatusUseCase;
import br.com.fiap.cheffy.infrastructure.adapters.in.mappers.InputOrderStatusMapper;
import br.com.fiap.cheffy.infrastructure.adapters.in.records.InputOrderStatusRecord;
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

    private final InputOrderStatusMapper inputOrderStatusMapper;

    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;

    @KafkaListener(
            topics = "${order.events.status-changes-topic:order.status-changes}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumer(InputOrderStatusRecord message) {
        log.info("Message received: [message={}]", message);
        updateOrderStatusUseCase.execute(inputOrderStatusMapper.toCommand(message));
        log.info("Message processed successfully: [orderId={}, status={}]", message.orderId(), message.status());
    }
}
