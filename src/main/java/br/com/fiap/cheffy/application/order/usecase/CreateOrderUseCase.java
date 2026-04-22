package br.com.fiap.cheffy.application.order.usecase;

import br.com.fiap.cheffy.application.order.dto.OrderCommandPort;
import br.com.fiap.cheffy.application.order.dto.OrderItemCommandPort;
import br.com.fiap.cheffy.domain.order.entity.Order;
import br.com.fiap.cheffy.domain.order.entity.OrderItem;
import br.com.fiap.cheffy.domain.order.port.input.CreateOrderInput;
import br.com.fiap.cheffy.domain.order.port.output.OrderRepository;

import java.util.List;
import java.util.UUID;

public class CreateOrderUseCase implements CreateOrderInput {

    private final OrderRepository orderRepository;

    public CreateOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public String execute(OrderCommandPort command, UUID customerId) {
        List<OrderItem> items = command.items()
                .stream()
                .map(this::toOrderItem)
                .toList();

        Order order = Order.create(customerId, command.restaurantId(), items);

        return orderRepository.save(order).getId().toString();
    }

    private OrderItem toOrderItem(OrderItemCommandPort item) {
        return OrderItem.create(
                item.foodItemId(),
                item.name(),
                item.quantity(),
                item.price()
        );
    }
}
