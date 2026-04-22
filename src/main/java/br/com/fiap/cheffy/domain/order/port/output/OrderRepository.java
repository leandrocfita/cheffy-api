package br.com.fiap.cheffy.domain.order.port.output;

import br.com.fiap.cheffy.domain.order.entity.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(UUID id);

    List<Order> findAllByCustomerId(UUID customerId);
}
