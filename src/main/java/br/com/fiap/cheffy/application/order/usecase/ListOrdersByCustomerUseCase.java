package br.com.fiap.cheffy.application.order.usecase;

import br.com.fiap.cheffy.application.order.dto.OrderQueryPort;
import br.com.fiap.cheffy.application.order.mapper.OrderQueryMapper;
import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.order.entity.Order;
import br.com.fiap.cheffy.domain.order.port.input.ListOrdersByCustomerInput;
import br.com.fiap.cheffy.domain.order.port.output.OrderRepository;

import java.util.UUID;

public class ListOrdersByCustomerUseCase implements ListOrdersByCustomerInput {

    private final OrderRepository orderRepository;
    private final OrderQueryMapper orderQueryMapper;

    public ListOrdersByCustomerUseCase(OrderRepository orderRepository, OrderQueryMapper orderQueryMapper) {
        this.orderRepository = orderRepository;
        this.orderQueryMapper = orderQueryMapper;
    }

    @Override
    public PageResult<OrderQueryPort> execute(UUID customerId, PageRequest pageRequest) {
        PageResult<Order> orders = orderRepository.findAllByCustomerId(customerId, pageRequest);
        return PageResult.from(orders, orders.content().stream().map(orderQueryMapper::toQueryPort).toList());
    }
}
