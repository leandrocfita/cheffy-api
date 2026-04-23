package br.com.fiap.cheffy.infrastructure.persistence.order.adapter;

import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.order.entity.Order;
import br.com.fiap.cheffy.domain.order.port.output.OrderRepository;
import br.com.fiap.cheffy.infrastructure.persistence.order.mapper.OrderPersistenceMapper;
import br.com.fiap.cheffy.infrastructure.persistence.order.repository.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public PageResult<Order> findAllByCustomerId(UUID customerId, PageRequest pageRequest) {
        Sort sort = pageRequest.direction() == PageRequest.SortDirection.DESC
                ? Sort.by(pageRequest.sortBy()).descending()
                : Sort.by(pageRequest.sortBy()).ascending();

        org.springframework.data.domain.PageRequest springPageRequest =
                org.springframework.data.domain.PageRequest.of(pageRequest.page(), pageRequest.size(), sort);

        Page<br.com.fiap.cheffy.infrastructure.persistence.order.entity.OrderJpaEntity> page =
                orderJpaRepository.findAllByCustomerId(customerId, springPageRequest);

        List<UUID> orderIds = page.getContent()
                .stream()
                .map(br.com.fiap.cheffy.infrastructure.persistence.order.entity.OrderJpaEntity::getId)
                .toList();

        if (orderIds.isEmpty()) {
            return PageResult.of(Collections.emptyList(), pageRequest.page(), pageRequest.size(), page.getTotalElements());
        }

        Map<UUID, br.com.fiap.cheffy.infrastructure.persistence.order.entity.OrderJpaEntity> ordersById =
                orderJpaRepository.findAllByIdInWithItems(orderIds)
                        .stream()
                        .collect(Collectors.toMap(
                                br.com.fiap.cheffy.infrastructure.persistence.order.entity.OrderJpaEntity::getId,
                                Function.identity(),
                                (first, ignored) -> first,
                                LinkedHashMap::new
                        ));

        List<Order> orders = orderIds.stream()
                .map(ordersById::get)
                .map(orderPersistenceMapper::toDomain)
                .toList();
        return PageResult.of(orders, pageRequest.page(), pageRequest.size(), page.getTotalElements());
    }
}
