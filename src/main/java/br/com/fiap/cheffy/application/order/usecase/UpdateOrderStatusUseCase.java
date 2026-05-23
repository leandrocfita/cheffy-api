package br.com.fiap.cheffy.application.order.usecase;

import br.com.fiap.cheffy.domain.order.entity.Order;
import br.com.fiap.cheffy.domain.order.exception.OrderNotFoundException;
import br.com.fiap.cheffy.domain.order.port.input.UpdateOrderStatusInput;
import br.com.fiap.cheffy.domain.order.port.output.OrderRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.ORDER_NOT_FOUND_EXCEPTION;

@Slf4j
public class UpdateOrderStatusUseCase implements UpdateOrderStatusInput {

    private final OrderRepository orderRepository;

    public UpdateOrderStatusUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void execute(UUID orderId, String status) {
        log.info("Received order status update request - orderId: {}, newStatus: {}", orderId, status);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND_EXCEPTION, orderId));

        log.debug("Order found for status update - orderId: {}", orderId);

        if (order.getStatus().name().equalsIgnoreCase(status)) {
            log.info("Order status is already '{}' - no update needed - orderId: {}", status, orderId);
            return;
        }

        switch (status.toUpperCase()) {
            case "PAYMENT_PENDING" -> {
                order.markPaymentPending();
                log.info("Order status changed to PAYMENT_PENDING - orderId: {}", orderId);
            }
            case "PAID" -> {
                order.markPaymentPending();
                log.info("Order status changed to PAID - orderId: {}", orderId);
            }
            default -> {
                log.error("Unsupported order status received - orderId: {}, status: {}", orderId, status);
                return;
            }
        }

        orderRepository.save(order);
        log.debug("Order status persisted - orderId: {}", orderId);
    }
}
