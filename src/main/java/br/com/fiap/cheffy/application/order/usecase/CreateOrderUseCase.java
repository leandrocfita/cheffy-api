package br.com.fiap.cheffy.application.order.usecase;

import br.com.fiap.cheffy.application.order.dto.CreateOrderResultPort;
import br.com.fiap.cheffy.application.order.dto.OrderCommandPort;
import br.com.fiap.cheffy.application.order.dto.OrderItemCommandPort;
import br.com.fiap.cheffy.application.fooditem.service.FoodItemServiceHelper;
import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.order.entity.Order;
import br.com.fiap.cheffy.domain.order.entity.OrderItem;
import br.com.fiap.cheffy.domain.order.port.input.CreateOrderInput;
import br.com.fiap.cheffy.domain.order.port.output.OrderRepository;

import java.util.List;
import java.util.UUID;

public class CreateOrderUseCase implements CreateOrderInput {

    private final OrderRepository orderRepository;
    private final FoodItemServiceHelper foodItemServiceHelper;

    public CreateOrderUseCase(OrderRepository orderRepository, FoodItemServiceHelper foodItemServiceHelper) {
        this.orderRepository = orderRepository;
        this.foodItemServiceHelper = foodItemServiceHelper;
    }

    @Override
    public CreateOrderResultPort execute(OrderCommandPort command, UUID customerId) {
        foodItemServiceHelper.getActiveRestaurantOrFail(command.restaurantId());

        List<OrderItem> items = command.items()
                .stream()
                .map(item -> toOrderItem(item, command.restaurantId()))
                .toList();

        Order order = Order.create(customerId, command.restaurantId(), items);

        Order savedOrder = orderRepository.save(order);

        return new CreateOrderResultPort(
                savedOrder.getId(),
                savedOrder.getTotalAmount().value()
        );
    }

    private OrderItem toOrderItem(OrderItemCommandPort item, UUID restaurantId) {
        FoodItem foodItem = foodItemServiceHelper.getAvailableFoodItemOrFail(item.foodItemId(), restaurantId);

        return OrderItem.create(
                foodItem.getId(),
                foodItem.getName(),
                item.quantity(),
                foodItem.getPrice().value()
        );
    }
}
