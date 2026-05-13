package br.com.fiap.cheffy.application.order.usecase;

import br.com.fiap.cheffy.application.order.dto.OrderQueryPort;
import br.com.fiap.cheffy.application.order.mapper.OrderQueryMapper;
import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.order.entity.Order;
import br.com.fiap.cheffy.domain.order.entity.OrderItem;
import br.com.fiap.cheffy.domain.order.entity.OrderStatus;
import br.com.fiap.cheffy.domain.order.exception.OrderNotFoundException;
import br.com.fiap.cheffy.domain.order.exception.OrderOperationNotAllowedException;
import br.com.fiap.cheffy.domain.order.port.output.OrderRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfirmOrderUseCaseTest {

    @Test
    void executeConfirmsOrderWhenItBelongsToCustomer() {
        UUID customerId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        StubOrderRepository repository = new StubOrderRepository(orderId, customerId, OrderStatus.CREATED);
        ConfirmOrderUseCase useCase = new ConfirmOrderUseCase(repository, new OrderQueryMapper());

        OrderQueryPort result = useCase.execute(orderId, customerId);

        assertThat(result.id()).isEqualTo(orderId);
        assertThat(result.customerId()).isEqualTo(customerId);
        assertThat(result.status()).isEqualTo(OrderStatus.PAYMENT_PENDING);
        assertThat(repository.savedOrder).isNotNull();
        assertThat(repository.savedOrder.getStatus()).isEqualTo(OrderStatus.PAYMENT_PENDING);
    }

    @Test
    void executeThrowsWhenOrderDoesNotBelongToCustomer() {
        UUID orderId = UUID.randomUUID();
        ConfirmOrderUseCase useCase = new ConfirmOrderUseCase(
                new StubOrderRepository(orderId, UUID.randomUUID(), OrderStatus.CREATED),
                new OrderQueryMapper()
        );

        assertThrows(OrderNotFoundException.class, () -> useCase.execute(orderId, UUID.randomUUID()));
    }

    @Test
    void executeThrowsWhenOrderCannotBeConfirmedInCurrentStatus() {
        UUID customerId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        ConfirmOrderUseCase useCase = new ConfirmOrderUseCase(
                new StubOrderRepository(orderId, customerId, OrderStatus.PAYMENT_PENDING),
                new OrderQueryMapper()
        );

        assertThrows(OrderOperationNotAllowedException.class, () -> useCase.execute(orderId, customerId));
    }

    private static class StubOrderRepository implements OrderRepository {

        private final Order order;
        private Order savedOrder;

        private StubOrderRepository(UUID orderId, UUID customerId, OrderStatus status) {
            this.order = Order.reconstitute(
                    orderId,
                    customerId,
                    UUID.randomUUID(),
                    List.of(OrderItem.create(UUID.randomUUID(), "Burger", 2, new BigDecimal("15.00"))),
                    status
            );
        }

        @Override
        public Order save(Order order) {
            this.savedOrder = order;
            return order;
        }

        @Override
        public Optional<Order> findById(UUID id) {
            return Optional.of(order);
        }

        @Override
        public PageResult<Order> findAllByCustomerId(UUID customerId, PageRequest pageRequest) {
            return PageResult.of(List.of(), pageRequest.page(), pageRequest.size(), 0);
        }
    }
}
