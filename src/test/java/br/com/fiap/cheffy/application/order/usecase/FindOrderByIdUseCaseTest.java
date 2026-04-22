package br.com.fiap.cheffy.application.order.usecase;

import br.com.fiap.cheffy.application.order.dto.OrderQueryPort;
import br.com.fiap.cheffy.application.order.mapper.OrderQueryMapper;
import br.com.fiap.cheffy.domain.order.entity.Order;
import br.com.fiap.cheffy.domain.order.entity.OrderItem;
import br.com.fiap.cheffy.domain.order.entity.OrderStatus;
import br.com.fiap.cheffy.domain.order.exception.OrderNotFoundException;
import br.com.fiap.cheffy.domain.order.port.output.OrderRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FindOrderByIdUseCaseTest {

    @Test
    void executeReturnsOrderWhenItBelongsToCustomer() {
        UUID customerId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        FindOrderByIdUseCase useCase = new FindOrderByIdUseCase(new StubOrderRepository(orderId, customerId), new OrderQueryMapper());

        OrderQueryPort result = useCase.execute(orderId, customerId);

        assertThat(result.id()).isEqualTo(orderId);
        assertThat(result.customerId()).isEqualTo(customerId);
        assertThat(result.totalAmount()).isEqualByComparingTo("30.00");
        assertThat(result.items()).hasSize(1);
    }

    @Test
    void executeThrowsWhenOrderDoesNotBelongToCustomer() {
        UUID orderId = UUID.randomUUID();
        FindOrderByIdUseCase useCase = new FindOrderByIdUseCase(
                new StubOrderRepository(orderId, UUID.randomUUID()),
                new OrderQueryMapper()
        );

        assertThrows(OrderNotFoundException.class, () -> useCase.execute(orderId, UUID.randomUUID()));
    }

    private static class StubOrderRepository implements OrderRepository {

        private final Order order;

        private StubOrderRepository(UUID orderId, UUID customerId) {
            this.order = Order.reconstitute(
                    orderId,
                    customerId,
                    UUID.randomUUID(),
                    List.of(OrderItem.create(UUID.randomUUID(), "Burger", 2, new BigDecimal("15.00"))),
                    OrderStatus.CREATED
            );
        }

        @Override
        public Order save(Order order) {
            return order;
        }

        @Override
        public Optional<Order> findById(UUID id) {
            return Optional.of(order);
        }

        @Override
        public List<Order> findAllByCustomerId(UUID customerId) {
            return List.of();
        }
    }
}
