package br.com.fiap.cheffy.application.order.mapper;

import br.com.fiap.cheffy.application.order.dto.OrderItemQueryPort;
import br.com.fiap.cheffy.application.order.dto.OrderQueryPort;
import br.com.fiap.cheffy.domain.order.entity.Order;

public class OrderQueryMapper {

    public OrderQueryPort toQueryPort(Order order) {
        return new OrderQueryPort(
                order.getId(),
                order.getCustomerId(),
                order.getRestaurantId(),
                order.getTotalAmount().value(),
                order.getStatus(),
                order.getItems().stream()
                        .map(item -> new OrderItemQueryPort(
                                item.getFoodItemId(),
                                item.getName(),
                                item.getQuantity(),
                                item.getPrice().value()
                        ))
                        .toList()
        );
    }
}
