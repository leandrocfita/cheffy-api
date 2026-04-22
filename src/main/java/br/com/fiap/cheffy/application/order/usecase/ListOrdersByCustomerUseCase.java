package br.com.fiap.cheffy.application.order.usecase;

import br.com.fiap.cheffy.application.order.dto.OrderQueryPort;
import br.com.fiap.cheffy.application.order.mapper.OrderQueryMapper;
import br.com.fiap.cheffy.domain.order.port.input.ListOrdersByCustomerInput;
import br.com.fiap.cheffy.domain.order.port.output.OrderRepository;

import java.util.List;
import java.util.UUID;

public class ListOrdersByCustomerUseCase implements ListOrdersByCustomerInput {

    private final OrderRepository orderRepository;
    private final OrderQueryMapper orderQueryMapper;

    public ListOrdersByCustomerUseCase(OrderRepository orderRepository, OrderQueryMapper orderQueryMapper) {
        this.orderRepository = orderRepository;
        this.orderQueryMapper = orderQueryMapper;
    }

    @Override
    public List<OrderQueryPort> execute(UUID customerId) {
        return orderRepository.findAllByCustomerId(customerId)
                .stream()
                .map(orderQueryMapper::toQueryPort)
                .toList();
    }
}
