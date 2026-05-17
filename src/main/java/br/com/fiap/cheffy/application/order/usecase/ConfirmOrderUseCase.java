package br.com.fiap.cheffy.application.order.usecase;

import br.com.fiap.cheffy.application.order.dto.OrderConfirmationCommandPort;
import br.com.fiap.cheffy.application.order.dto.OrderQueryPort;
import br.com.fiap.cheffy.application.order.mapper.OrderQueryMapper;
import br.com.fiap.cheffy.domain.order.entity.Order;
import br.com.fiap.cheffy.domain.order.exception.OrderNotFoundException;
import br.com.fiap.cheffy.domain.order.port.input.ConfirmOrderInput;
import br.com.fiap.cheffy.domain.order.port.output.OrderConfirmationExternalClient;
import br.com.fiap.cheffy.domain.order.port.output.OrderRepository;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.ORDER_NOT_FOUND_EXCEPTION;

public class ConfirmOrderUseCase implements ConfirmOrderInput {

    private final OrderRepository orderRepository;
    private final OrderConfirmationExternalClient orderConfirmationExternalClient;
    private final OrderQueryMapper orderQueryMapper;
    private static final Logger logger = Logger.getLogger(ConfirmOrderUseCase.class.getName());

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
        if (logger.isLoggable(Level.INFO)) {
            logger.info("Starting order confirmation - orderId: " + orderId);
        }

        Order order = orderRepository.findById(orderId)
                .filter(savedOrder -> savedOrder.getCustomerId().equals(customerId))
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND_EXCEPTION, orderId));

        order.markPaymentPending();

        try {
            orderConfirmationExternalClient.confirm(new OrderConfirmationCommandPort(
                    order.getId(),
                    order.getTotalAmount().value(),
                    authorizationHeader
            ));
            logger.info("Payment confirmation successful");
        } catch (Exception ex) {
            logger.severe("Payment confirmation failed: " + ex.getMessage());
            throw ex;
        }
        var result = orderQueryMapper.toQueryPort(orderRepository.save(order));
        logger.info("Order confirmation completed");

        return result;
    }
}
