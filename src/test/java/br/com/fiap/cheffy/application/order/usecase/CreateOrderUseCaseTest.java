package br.com.fiap.cheffy.application.order.usecase;

import br.com.fiap.cheffy.application.order.dto.OrderCommandPort;
import br.com.fiap.cheffy.application.order.dto.OrderItemCommandPort;
import br.com.fiap.cheffy.domain.order.entity.Order;
import br.com.fiap.cheffy.domain.order.entity.OrderItem;
import br.com.fiap.cheffy.domain.order.entity.OrderStatus;
import br.com.fiap.cheffy.domain.order.port.output.OrderRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CreateOrderUseCaseTest {

    @Test
    void executeCreatesOrderAndReturnsSavedId() {
        InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();
        CreateOrderUseCase useCase = new CreateOrderUseCase(orderRepository);

        UUID customerId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        UUID foodItemId = UUID.randomUUID();

        OrderCommandPort command = new OrderCommandPort(
                restaurantId,
                List.of(new OrderItemCommandPort(foodItemId, "Burger", 2, new BigDecimal("15.00")))
        );

        String result = useCase.execute(command, customerId);

        Order savedOrder = orderRepository.lastSavedOrder;

        assertThat(result).isEqualTo(orderRepository.savedId.toString());
        assertThat(savedOrder.getCustomerId()).isEqualTo(customerId);
        assertThat(savedOrder.getRestaurantId()).isEqualTo(restaurantId);
        assertThat(savedOrder.getStatus()).isEqualTo(OrderStatus.CREATED);
        assertThat(savedOrder.getTotalAmount().value()).isEqualByComparingTo("30.00");

        OrderItem savedItem = savedOrder.getItems().getFirst();
        assertThat(savedItem.getFoodItemId()).isEqualTo(foodItemId);
        assertThat(savedItem.getName()).isEqualTo("Burger");
        assertThat(savedItem.getQuantity()).isEqualTo(2);
        assertThat(savedItem.getPrice().value()).isEqualByComparingTo("15.00");
    }

    private static class InMemoryOrderRepository implements OrderRepository {

        private final UUID savedId = UUID.randomUUID();
        private Order lastSavedOrder;

        @Override
        public Order save(Order order) {
            this.lastSavedOrder = order;
            return Order.reconstitute(
                    savedId,
                    order.getCustomerId(),
                    order.getRestaurantId(),
                    order.getItems(),
                    order.getStatus()
            );
        }

        @Override
        public Optional<Order> findById(UUID id) {
            return Optional.empty();
        }

        @Override
        public List<Order> findAllByCustomerId(UUID customerId) {
            return List.of();
        }
    }
}
