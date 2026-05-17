package br.com.fiap.cheffy.application.order.usecase;

import br.com.fiap.cheffy.application.order.dto.OrderConfirmationCommandPort;
import br.com.fiap.cheffy.application.order.dto.OrderQueryPort;
import br.com.fiap.cheffy.application.order.mapper.OrderQueryMapper;
import br.com.fiap.cheffy.domain.order.entity.Order;
import br.com.fiap.cheffy.domain.order.exception.OrderNotFoundException;
import br.com.fiap.cheffy.domain.order.port.input.ConfirmOrderInput;
import br.com.fiap.cheffy.domain.order.port.output.OrderConfirmationExternalClient;
import br.com.fiap.cheffy.domain.order.port.output.OrderRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.ORDER_NOT_FOUND_EXCEPTION;

@Slf4j
public class ConfirmOrderUseCase implements ConfirmOrderInput {

    private final OrderRepository orderRepository;
    private final OrderConfirmationExternalClient orderConfirmationExternalClient;
    private final OrderQueryMapper orderQueryMapper;

    public ConfirmOrderUseCase(
            OrderRepository orderRepository,
            OrderConfirmationExternalClient orderConfirmationExternalClient,
            OrderQueryMapper orderQueryMapper
    ) {
        this.orderRepository = orderRepository;
        this.orderConfirmationExternalClient = orderConfirmationExternalClient;
        this.orderQueryMapper = orderQueryMapper;
    }

    @Override
    public OrderQueryPort execute(UUID orderId, UUID customerId, String authorizationHeader) {
        log.info("Starting order confirmation - orderId: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .filter(savedOrder -> savedOrder.getCustomerId().equals(customerId))
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND_EXCEPTION, orderId));

        order.markPaymentPending();

        orderConfirmationExternalClient.confirm(new OrderConfirmationCommandPort(
                order.getId(),
                order.getTotalAmount().value(),
                authorizationHeader
        ));
        var result = orderQueryMapper.toQueryPort(orderRepository.save(order));
        log.info("Order confirmation completed");
        return result;
    }
}
