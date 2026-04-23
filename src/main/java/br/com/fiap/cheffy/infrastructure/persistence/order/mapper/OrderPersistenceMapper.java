package br.com.fiap.cheffy.infrastructure.persistence.order.mapper;

import br.com.fiap.cheffy.domain.order.entity.Order;
import br.com.fiap.cheffy.domain.order.entity.OrderItem;
import br.com.fiap.cheffy.domain.order.entity.OrderStatus;
import br.com.fiap.cheffy.infrastructure.persistence.order.entity.OrderItemJpaEntity;
import br.com.fiap.cheffy.infrastructure.persistence.order.entity.OrderJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderPersistenceMapper {

    public Order toDomain(OrderJpaEntity entity) {
        List<OrderItem> items = entity.getItems()
                .stream()
                .map(this::toDomainItem)
                .toList();

        return Order.reconstitute(
                entity.getId(),
                entity.getCustomerId(),
                entity.getRestaurantId(),
                items,
                OrderStatus.valueOf(entity.getStatus())
        );
    }

    public OrderJpaEntity toJpa(Order domain) {
        OrderJpaEntity entity = new OrderJpaEntity();
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        entity.setCustomerId(domain.getCustomerId());
        entity.setRestaurantId(domain.getRestaurantId());
        entity.setTotalAmount(domain.getTotalAmount().value());
        entity.setStatus(domain.getStatus().name());

        List<OrderItemJpaEntity> items = domain.getItems()
                .stream()
                .map(item -> toJpaItem(item, entity))
                .toList();

        entity.getItems().clear();
        entity.getItems().addAll(items);

        return entity;
    }

    private OrderItem toDomainItem(OrderItemJpaEntity entity) {
        return OrderItem.create(
                entity.getFoodItemId(),
                entity.getName(),
                entity.getQuantity(),
                entity.getPrice()
        );
    }

    private OrderItemJpaEntity toJpaItem(OrderItem domain, OrderJpaEntity order) {
        OrderItemJpaEntity entity = new OrderItemJpaEntity();
        entity.setFoodItemId(domain.getFoodItemId());
        entity.setName(domain.getName());
        entity.setQuantity(domain.getQuantity());
        entity.setPrice(domain.getPrice().value());
        entity.setOrder(order);
        return entity;
    }
}
