package br.com.fiap.cheffy.application.order.usecase;

import br.com.fiap.cheffy.application.order.dto.CreateOrderResultPort;
import br.com.fiap.cheffy.application.order.dto.OrderCommandPort;
import br.com.fiap.cheffy.application.order.dto.OrderCreatedEventPort;
import br.com.fiap.cheffy.application.order.dto.OrderItemCommandPort;
import br.com.fiap.cheffy.application.fooditem.service.FoodItemServiceHelper;
import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.order.entity.Order;
import br.com.fiap.cheffy.domain.order.entity.OrderItem;
import br.com.fiap.cheffy.domain.order.entity.OrderStatus;
import br.com.fiap.cheffy.domain.order.port.output.OrderCreatedEventPublisher;
import br.com.fiap.cheffy.domain.order.port.output.OrderRepository;
import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CreateOrderUseCaseTest {

    @Test
    void executeCreatesOrderAndReturnsSavedIdAndTotalAmount() {
        InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();
        InMemoryOrderCreatedEventPublisher eventPublisher = new InMemoryOrderCreatedEventPublisher();
        FoodItemServiceHelper foodItemServiceHelper = new FoodItemServiceHelper(
                new StubFoodItemRepository(),
                new StubRestaurantRepository()
        );
        CreateOrderUseCase useCase = new CreateOrderUseCase(orderRepository, foodItemServiceHelper, eventPublisher);

        UUID customerId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        UUID foodItemId = UUID.randomUUID();

        OrderCommandPort command = new OrderCommandPort(
                restaurantId,
                List.of(new OrderItemCommandPort(foodItemId, "payload name", 2, new BigDecimal("999.00")))
        );

        CreateOrderResultPort result = useCase.execute(command, customerId);

        Order savedOrder = orderRepository.lastSavedOrder;

        assertThat(result.orderId()).isEqualTo(orderRepository.savedId);
        assertThat(result.totalAmount()).isEqualByComparingTo("30.00");
        assertThat(savedOrder.getCustomerId()).isEqualTo(customerId);
        assertThat(savedOrder.getRestaurantId()).isEqualTo(restaurantId);
        assertThat(savedOrder.getStatus()).isEqualTo(OrderStatus.CREATED);
        assertThat(savedOrder.getTotalAmount().value()).isEqualByComparingTo("30.00");

        OrderItem savedItem = savedOrder.getItems().getFirst();
        assertThat(savedItem.getFoodItemId()).isEqualTo(foodItemId);
        assertThat(savedItem.getName()).isEqualTo("Burger");
        assertThat(savedItem.getQuantity()).isEqualTo(2);
        assertThat(savedItem.getPrice().value()).isEqualByComparingTo("15.00");
        assertThat(eventPublisher.publishedEvent).isNotNull();
        assertThat(eventPublisher.publishedEvent.orderId()).isEqualTo(orderRepository.savedId);
        assertThat(eventPublisher.publishedEvent.customerId()).isEqualTo(customerId);
        assertThat(eventPublisher.publishedEvent.restaurantId()).isEqualTo(restaurantId);
        assertThat(eventPublisher.publishedEvent.totalAmount()).isEqualByComparingTo("30.00");
        assertThat(eventPublisher.publishedEvent.items()).hasSize(1);
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

    private static class InMemoryOrderCreatedEventPublisher implements OrderCreatedEventPublisher {

        private OrderCreatedEventPort publishedEvent;

        @Override
        public void publish(OrderCreatedEventPort event) {
            this.publishedEvent = event;
        }
    }

    private static class StubFoodItemRepository implements br.com.fiap.cheffy.domain.fooditem.port.output.FoodItemRepository {

        @Override
        public FoodItem save(FoodItem foodItem) {
            return foodItem;
        }

        @Override
        public Optional<FoodItem> findById(UUID foodItemId) {
            return Optional.empty();
        }

        @Override
        public Optional<FoodItem> findByIdAndRestaurantId(UUID foodItemId, UUID restaurantId) {
            return Optional.of(
                    FoodItem.reconstitute(foodItemId, "Burger", "Delicious", new BigDecimal("15.00"), "photo", true, true, true)
            );
        }

        @Override
        public List<FoodItem> findAllByRestaurantId(UUID restaurantId) {
            return List.of();
        }

        @Override
        public br.com.fiap.cheffy.domain.common.PageResult<FoodItem> findAllByRestaurantId(UUID restaurantId, br.com.fiap.cheffy.domain.common.PageRequest pageRequest) {
            return null;
        }

        @Override
        public br.com.fiap.cheffy.domain.common.PageResult<FoodItem> findAllActiveByRestaurantId(UUID restaurantId, br.com.fiap.cheffy.domain.common.PageRequest pageRequest) {
            return null;
        }

        @Override
        public boolean existsInRestaurantById(UUID restaurantId, UUID foodItemId) {
            return false;
        }

        @Override
        public boolean existsByNameIgnoreCaseAndRestaurantId(String foodName, UUID restaurantId) {
            return false;
        }
    }

    private static class StubRestaurantRepository implements br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository {

        @Override
        public Restaurant save(Restaurant restaurant) {
            return restaurant;
        }

        @Override
        public boolean existsByCnpj(String cnpj) {
            return false;
        }

        @Override
        public boolean existsByName(String restaurantName) {
            return false;
        }

        @Override
        public boolean existsActiveRestaurantByUserId(UUID userId) {
            return false;
        }

        @Override
        public Optional<Restaurant> findById(UUID restaurantId) {
            return Optional.of(Restaurant.reconstitute(
                    restaurantId,
                    "Test Restaurant",
                    "12345678000199",
                    "Burger",
                    ZoneId.systemDefault(),
                    null,
                    null,
                    true,
                    true,
                    null,
                    null,
                    new br.com.fiap.cheffy.domain.restaurant.entity.Menu(new java.util.HashSet<>())
            ));
        }
    }
}
