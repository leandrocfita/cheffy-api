package br.com.fiap.cheffy.infrastructure.persistence.order.adapter;

import br.com.fiap.cheffy.domain.order.entity.Order;
import br.com.fiap.cheffy.domain.order.port.output.OrderRepository;
import br.com.fiap.cheffy.infrastructure.persistence.order.mapper.OrderPersistenceMapper;
import br.com.fiap.cheffy.infrastructure.persistence.order.repository.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderPersistenceMapper orderPersistenceMapper;

    @Override
    public Order save(Order order) {
        return orderPersistenceMapper.toDomain(
                orderJpaRepository.save(orderPersistenceMapper.toJpa(order))
        );
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return orderJpaRepository.findByIdWithItems(id)
                .map(orderPersistenceMapper::toDomain);
    }

    @Override
    public List<Order> findAllByCustomerId(UUID customerId) {
        return orderJpaRepository.findAllByCustomerIdWithItems(customerId)
                .stream()
                .map(orderPersistenceMapper::toDomain)
                .toList();
    }
}
