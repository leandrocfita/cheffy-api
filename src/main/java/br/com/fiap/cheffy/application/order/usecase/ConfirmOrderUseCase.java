package br.com.fiap.cheffy.application.order.usecase;

import br.com.fiap.cheffy.application.order.dto.OrderCreatedEventPort;
import br.com.fiap.cheffy.application.order.dto.OrderQueryPort;
import br.com.fiap.cheffy.application.order.mapper.OrderQueryMapper;
import br.com.fiap.cheffy.domain.order.entity.Order;
import br.com.fiap.cheffy.domain.order.exception.OrderNotFoundException;
import br.com.fiap.cheffy.domain.order.port.input.ConfirmOrderInput;
import br.com.fiap.cheffy.domain.order.port.output.OrderEventPublisher;
import br.com.fiap.cheffy.domain.order.port.output.OrderRepository;

import java.util.UUID;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.ORDER_NOT_FOUND_EXCEPTION;

public class ConfirmOrderUseCase implements ConfirmOrderInput {

    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;
    private final OrderQueryMapper orderQueryMapper;

    public ConfirmOrderUseCase(
            OrderRepository orderRepository,
            OrderEventPublisher orderEventPublisher,
            OrderQueryMapper orderQueryMapper
    ) {
        this.orderRepository = orderRepository;
        this.orderEventPublisher = orderEventPublisher;
        this.orderQueryMapper = orderQueryMapper;
    }

    @Override
    public OrderQueryPort execute(UUID orderId, UUID customerId) {
        Order order = orderRepository.findById(orderId)
                .filter(savedOrder -> savedOrder.getCustomerId().equals(customerId))
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND_EXCEPTION, orderId));

        order.markPaymentPending();
        orderEventPublisher.publishOrderCreated(new OrderCreatedEventPort(
                order.getId(),
                order.getTotalAmount().value()
        ));

        return orderQueryMapper.toQueryPort(orderRepository.save(order));
    }
}
