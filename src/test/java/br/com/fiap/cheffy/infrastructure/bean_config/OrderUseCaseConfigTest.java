package br.com.fiap.cheffy.infrastructure.bean_config;

import br.com.fiap.cheffy.application.fooditem.service.FoodItemServiceHelper;
import br.com.fiap.cheffy.application.order.mapper.OrderQueryMapper;
import br.com.fiap.cheffy.application.order.usecase.CreateOrderUseCase;
import br.com.fiap.cheffy.application.order.usecase.FindOrderByIdUseCase;
import br.com.fiap.cheffy.application.order.usecase.ListOrdersByCustomerUseCase;
import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.fooditem.port.output.FoodItemRepository;
import br.com.fiap.cheffy.domain.order.entity.Order;
import br.com.fiap.cheffy.domain.order.port.output.OrderRepository;
import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class OrderUseCaseConfigTest {

    private final OrderQueryMapper orderQueryMapper = new OrderQueryMapper();

    @Test
    void createOrderUseCaseCreatesBean() {
        OrderUseCaseConfig config = new OrderUseCaseConfig();

        CreateOrderUseCase useCase = config.createOrderUseCase(new StubOrderRepository(), new StubFoodItemServiceHelper());

        assertThat(useCase).isNotNull();
    }

    @Test
    void findOrderByIdUseCaseCreatesBean() {
        OrderUseCaseConfig config = new OrderUseCaseConfig();

        FindOrderByIdUseCase useCase = config.findOrderByIdUseCase(new StubOrderRepository(), orderQueryMapper);

        assertThat(useCase).isNotNull();
    }

    @Test
    void listOrdersByCustomerUseCaseCreatesBean() {
        OrderUseCaseConfig config = new OrderUseCaseConfig();

        ListOrdersByCustomerUseCase useCase = config.listOrdersByCustomerUseCase(new StubOrderRepository(), orderQueryMapper);

        assertThat(useCase).isNotNull();
    }

    private static class StubOrderRepository implements OrderRepository {

        @Override
        public Order save(Order order) {
            return order;
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

    private static class StubFoodItemServiceHelper extends FoodItemServiceHelper {

        StubFoodItemServiceHelper() {
            super(new StubFoodItemRepository(), new StubRestaurantRepository());
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
            return Optional.empty();
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
            return Optional.empty();
        }
    }
}
