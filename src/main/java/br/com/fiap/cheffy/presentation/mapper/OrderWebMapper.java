package br.com.fiap.cheffy.presentation.mapper;

import br.com.fiap.cheffy.application.order.dto.OrderCommandPort;
import br.com.fiap.cheffy.application.order.dto.OrderItemCommandPort;
import br.com.fiap.cheffy.presentation.dto.OrderCreateDTO;
import org.springframework.stereotype.Component;

@Component
public class OrderWebMapper {

    public OrderCommandPort toCommand(OrderCreateDTO dto) {
        return new OrderCommandPort(
                dto.restaurantId(),
                dto.items().stream()
                        .map(item -> new OrderItemCommandPort(
                                item.foodItemId(),
                                item.quantity()
                        ))
                        .toList()
        );
    }
}
