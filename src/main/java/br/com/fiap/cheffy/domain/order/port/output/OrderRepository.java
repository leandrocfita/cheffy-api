package br.com.fiap.cheffy.domain.order.port.output;

import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.order.entity.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(UUID id);

    PageResult<Order> findAllByCustomerId(UUID customerId, PageRequest pageRequest);
}
