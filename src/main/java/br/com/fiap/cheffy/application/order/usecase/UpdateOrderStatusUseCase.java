package br.com.fiap.cheffy.application.order.usecase;

import br.com.fiap.cheffy.application.order.ports.in.records.OrderStatusCommandRecord;
import br.com.fiap.cheffy.domain.order.entity.Order;
import br.com.fiap.cheffy.domain.order.entity.OrderStatus;
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
    public void execute(OrderStatusCommandRecord request) {
        log.info("Received order status update request - orderId: {}, newStatus: {}", request.orderId(), request.status());
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND_EXCEPTION, request.orderId()));

        log.debug("Order found for status update - orderId: {}", request.orderId());

        if (order.getStatus().name().equalsIgnoreCase(request.status())) {
            log.info("Order status is already '{}' - no update needed - orderId: {}", request.status(), request.orderId());
            return;
        }

        OrderStatus newStatus = OrderStatus.fromStatus(request.status());
        order.markNewStatus(newStatus);
        log.info("Order status updated to {} - orderId: {}", newStatus, request.orderId());

        orderRepository.save(order);
        log.debug("Order status persisted - orderId: {}", request.orderId());
    }
}
