package br.com.fiap.cheffy.application.order.usecase;

import br.com.fiap.cheffy.application.order.dto.OrderQueryPort;
import br.com.fiap.cheffy.application.order.mapper.OrderQueryMapper;
import br.com.fiap.cheffy.domain.order.entity.Order;
import br.com.fiap.cheffy.domain.order.exception.OrderNotFoundException;
import br.com.fiap.cheffy.domain.order.port.input.FindOrderByIdInput;
import br.com.fiap.cheffy.domain.order.port.output.OrderRepository;

import java.util.UUID;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.ORDER_NOT_FOUND_EXCEPTION;

public class FindOrderByIdUseCase implements FindOrderByIdInput {

    private final OrderRepository orderRepository;
    private final OrderQueryMapper orderQueryMapper;

    public FindOrderByIdUseCase(OrderRepository orderRepository, OrderQueryMapper orderQueryMapper) {
        this.orderRepository = orderRepository;
        this.orderQueryMapper = orderQueryMapper;
    }

    @Override
    public OrderQueryPort execute(UUID orderId, UUID customerId) {
        Order order = orderRepository.findById(orderId)
                .filter(savedOrder -> savedOrder.getCustomerId().equals(customerId))
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND_EXCEPTION, orderId));

        return orderQueryMapper.toQueryPort(order);
    }
}
