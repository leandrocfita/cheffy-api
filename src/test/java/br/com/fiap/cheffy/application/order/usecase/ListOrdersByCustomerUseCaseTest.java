package br.com.fiap.cheffy.application.order.usecase;

import br.com.fiap.cheffy.application.order.dto.OrderQueryPort;
import br.com.fiap.cheffy.application.order.mapper.OrderQueryMapper;
import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
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

class ListOrdersByCustomerUseCaseTest {

    @Test
    void executeReturnsMappedOrders() {
        UUID customerId = UUID.randomUUID();
        ListOrdersByCustomerUseCase useCase = new ListOrdersByCustomerUseCase(new StubOrderRepository(customerId), new OrderQueryMapper());

        PageRequest pageRequest = PageRequest.of(0, 10, "dateCreated", PageRequest.SortDirection.DESC);

        PageResult<OrderQueryPort> result = useCase.execute(customerId, pageRequest);

        assertThat(result.content()).hasSize(2);
        assertThat(result.content().getFirst().customerId()).isEqualTo(customerId);
        assertThat(result.content().getFirst().items()).hasSize(1);
    }

    private static class StubOrderRepository implements OrderRepository {

        private final List<Order> orders;

        private StubOrderRepository(UUID customerId) {
            this.orders = List.of(
                    Order.reconstitute(
                            UUID.randomUUID(),
                            customerId,
                            UUID.randomUUID(),
                            List.of(OrderItem.create(UUID.randomUUID(), "Burger", 2, new BigDecimal("15.00"))),
                            OrderStatus.CREATED
                    ),
                    Order.reconstitute(
                            UUID.randomUUID(),
                            customerId,
                            UUID.randomUUID(),
                            List.of(OrderItem.create(UUID.randomUUID(), "Soda", 1, new BigDecimal("7.50"))),
                            OrderStatus.PAID
                    )
            );
        }

        @Override
        public Order save(Order order) {
            return order;
        }

        @Override
        public Optional<Order> findById(UUID id) {
            return Optional.empty();
        }

        @Override
        public PageResult<Order> findAllByCustomerId(UUID customerId, PageRequest pageRequest) {
            return PageResult.of(orders, pageRequest.page(), pageRequest.size(), orders.size());
        }
    }
}
