package br.com.fiap.cheffy.application.order.usecase;

import br.com.fiap.cheffy.application.order.dto.CreateOrderResultPort;
import br.com.fiap.cheffy.application.order.dto.OrderCommandPort;
import br.com.fiap.cheffy.application.order.dto.OrderItemCommandPort;
import br.com.fiap.cheffy.application.fooditem.service.FoodItemServiceHelper;
import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.fooditem.exception.FoodItemUnavailableForOrderException;
import br.com.fiap.cheffy.domain.fooditem.port.output.FoodItemRepository;
import br.com.fiap.cheffy.domain.order.entity.Order;
import br.com.fiap.cheffy.domain.order.entity.OrderItem;
import br.com.fiap.cheffy.domain.order.entity.OrderStatus;
import br.com.fiap.cheffy.domain.order.port.output.OrderRepository;
import br.com.fiap.cheffy.domain.restaurant.entity.Menu;
import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreateOrderUseCaseTest {

    @Test
    void executeCreatesOrderAndReturnsSavedIdAndTotalAmount() {
        InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();
        FoodItemServiceHelper foodItemServiceHelper = new FoodItemServiceHelper(
                new StubFoodItemRepository(),
                new StubRestaurantRepository()
        );
        CreateOrderUseCase useCase = new CreateOrderUseCase(orderRepository, foodItemServiceHelper);

        UUID customerId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        UUID foodItemId = UUID.randomUUID();

        OrderCommandPort command = new OrderCommandPort(
                restaurantId,
                List.of(new OrderItemCommandPort(foodItemId, 2))
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
    }

    @Test
    void executeThrowsWhenFoodItemIsUnavailable() {
        InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();
        FoodItemServiceHelper foodItemServiceHelper = new FoodItemServiceHelper(
                new UnavailableFoodItemRepository(),
                new StubRestaurantRepository()
        );
        CreateOrderUseCase useCase = new CreateOrderUseCase(orderRepository, foodItemServiceHelper);

        UUID customerId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        UUID foodItemId = UUID.randomUUID();

        OrderCommandPort command = new OrderCommandPort(
                restaurantId,
                List.of(new OrderItemCommandPort(foodItemId, 1))
        );

        assertThrows(FoodItemUnavailableForOrderException.class, () -> useCase.execute(command, customerId));
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
        public PageResult<Order> findAllByCustomerId(
                UUID customerId,
                PageRequest pageRequest
        ) {
            return PageResult.of(List.of(), pageRequest.page(), pageRequest.size(), 0);
        }
    }

    private static class StubFoodItemRepository implements FoodItemRepository {

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
        public PageResult<FoodItem> findAllByRestaurantId(UUID restaurantId, PageRequest pageRequest) {
            return null;
        }

        @Override
        public PageResult<FoodItem> findAllActiveByRestaurantId(UUID restaurantId, PageRequest pageRequest) {
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

    private static class UnavailableFoodItemRepository extends StubFoodItemRepository {

        @Override
        public Optional<FoodItem> findByIdAndRestaurantId(UUID foodItemId, UUID restaurantId) {
            return Optional.of(
                    FoodItem.reconstitute(foodItemId, "Burger", "Delicious", new BigDecimal("15.00"), "photo", true, false, true)
            );
        }
    }

    private static class StubRestaurantRepository implements RestaurantRepository {

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
                    new Menu(new HashSet<>())
            ));
        }
    }
}
