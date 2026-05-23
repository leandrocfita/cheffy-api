package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.application.order.dto.CreateOrderResultPort;
import br.com.fiap.cheffy.application.order.dto.OrderCommandPort;
import br.com.fiap.cheffy.application.order.dto.OrderItemQueryPort;
import br.com.fiap.cheffy.application.order.dto.OrderQueryPort;
import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.order.port.input.ConfirmOrderInput;
import br.com.fiap.cheffy.domain.order.port.input.CreateOrderInput;
import br.com.fiap.cheffy.domain.order.port.input.FindOrderByIdInput;
import br.com.fiap.cheffy.domain.order.port.input.ListOrdersByCustomerInput;
import br.com.fiap.cheffy.domain.order.entity.OrderStatus;
import br.com.fiap.cheffy.infrastructure.security.resolver.CurrentUserMapper;
import br.com.fiap.cheffy.presentation.dto.OrderCreateDTO;
import br.com.fiap.cheffy.presentation.dto.OrderItemDTO;
import br.com.fiap.cheffy.presentation.mapper.OrderWebMapper;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class OrderControllerTest {

    private static final String TOKEN_VALUE = "user-token";

    @Test
    void createOrderReturnsCreatedWithOrderIdAndTotalAmount() {
        InMemoryCreateOrderInput createOrderInput = new InMemoryCreateOrderInput();
        OrderController controller = new OrderController(
                createOrderInput,
                new InMemoryConfirmOrderInput(),
                new InMemoryFindOrderByIdInput(),
                new InMemoryListOrdersByCustomerInput(),
                new OrderWebMapper(),
                new CurrentUserMapper()
        );

        UUID userId = UUID.randomUUID();
        Jwt jwt = jwtFor(userId);
        UUID restaurantId = UUID.randomUUID();
        UUID foodItemId = UUID.randomUUID();

        OrderCreateDTO dto = new OrderCreateDTO(
                restaurantId,
                List.of(new OrderItemDTO(foodItemId, 2))
        );

        ResponseEntity<CreateOrderResultPort> response = controller.createOrder(dto, jwt);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().orderId()).isEqualTo(createOrderInput.createdId);
        assertThat(response.getBody().totalAmount()).isEqualByComparingTo("30.00");
        assertThat(createOrderInput.customerId).isEqualTo(userId);
        assertThat(createOrderInput.command.restaurantId()).isEqualTo(restaurantId);
        assertThat(createOrderInput.command.items()).hasSize(1);
        assertThat(createOrderInput.command.items().getFirst().foodItemId()).isEqualTo(foodItemId);
    }

    @Test
    void findByIdReturnsOrder() {
        InMemoryFindOrderByIdInput findOrderByIdInput = new InMemoryFindOrderByIdInput();
        OrderController controller = new OrderController(
                new InMemoryCreateOrderInput(),
                new InMemoryConfirmOrderInput(),
                findOrderByIdInput,
                new InMemoryListOrdersByCustomerInput(),
                new OrderWebMapper(),
                new CurrentUserMapper()
        );

        UUID userId = UUID.randomUUID();
        Jwt jwt = jwtFor(userId);
        UUID orderId = UUID.randomUUID();

        ResponseEntity<OrderQueryPort> response = controller.findById(orderId, jwt);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(orderId);
        assertThat(findOrderByIdInput.customerId).isEqualTo(userId);
    }

    @Test
    void confirmOrderReturnsConfirmedOrder() {
        InMemoryConfirmOrderInput confirmOrderInput = new InMemoryConfirmOrderInput();
        OrderController controller = new OrderController(
                new InMemoryCreateOrderInput(),
                confirmOrderInput,
                new InMemoryFindOrderByIdInput(),
                new InMemoryListOrdersByCustomerInput(),
                new OrderWebMapper(),
                new CurrentUserMapper()
        );

        UUID userId = UUID.randomUUID();
        Jwt jwt = jwtFor(userId);
        UUID orderId = UUID.randomUUID();

        ResponseEntity<OrderQueryPort> response = controller.confirmOrder(orderId, jwt);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(orderId);
        assertThat(response.getBody().status()).isEqualTo(OrderStatus.PAYMENT_PENDING);
        assertThat(confirmOrderInput.customerId).isEqualTo(userId);
    }

    @Test
    void listByCustomerReturnsOrders() {
        InMemoryListOrdersByCustomerInput listOrdersByCustomerInput = new InMemoryListOrdersByCustomerInput();
        OrderController controller = new OrderController(
                new InMemoryCreateOrderInput(),
                new InMemoryConfirmOrderInput(),
                new InMemoryFindOrderByIdInput(),
                listOrdersByCustomerInput,
                new OrderWebMapper(),
                new CurrentUserMapper()
        );

        UUID userId = UUID.randomUUID();
        Jwt jwt = jwtFor(userId);

        ResponseEntity<PageResult<OrderQueryPort>> response = controller.listByCustomer(
                jwt,
                0,
                10,
                "dateCreated",
                Sort.Direction.DESC
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().content()).hasSize(1);
        assertThat(listOrdersByCustomerInput.customerId).isEqualTo(userId);
        assertThat(listOrdersByCustomerInput.pageRequest.sortBy()).isEqualTo("dateCreated");
        assertThat(listOrdersByCustomerInput.pageRequest.direction()).isEqualTo(PageRequest.SortDirection.DESC);
    }

    private static Jwt jwtFor(UUID userId) {
        return new Jwt(
                TOKEN_VALUE,
                Instant.now(),
                Instant.now().plusSeconds(3600),
                Map.of("alg", "none"),
                Map.of("sub", userId.toString(), "login", "customer")
        );
    }

    private static class InMemoryCreateOrderInput implements CreateOrderInput {

        private final UUID createdId = UUID.randomUUID();
        private OrderCommandPort command;
        private UUID customerId;

        @Override
        public CreateOrderResultPort execute(OrderCommandPort command, UUID customerId) {
            this.command = command;
            this.customerId = customerId;
            return new CreateOrderResultPort(createdId, new BigDecimal("30.00"));
        }
    }

    private static class InMemoryConfirmOrderInput implements ConfirmOrderInput {

        private UUID customerId;

        @Override
        public OrderQueryPort execute(UUID orderId, UUID customerId) {
            this.customerId = customerId;
            return new OrderQueryPort(
                    orderId,
                    customerId,
                    UUID.randomUUID(),
                    new BigDecimal("30.00"),
                    OrderStatus.PAYMENT_PENDING,
                    List.of(new OrderItemQueryPort(UUID.randomUUID(), "Burger", 2, new BigDecimal("15.00")))
            );
        }
    }

    private static class InMemoryFindOrderByIdInput implements FindOrderByIdInput {

        private UUID customerId;

        @Override
        public OrderQueryPort execute(UUID orderId, UUID customerId) {
            this.customerId = customerId;
            return new OrderQueryPort(
                    orderId,
                    customerId,
                    UUID.randomUUID(),
                    new BigDecimal("30.00"),
                    OrderStatus.CREATED,
                    List.of(new OrderItemQueryPort(UUID.randomUUID(), "Burger", 2, new BigDecimal("15.00")))
            );
        }
    }

    private static class InMemoryListOrdersByCustomerInput implements ListOrdersByCustomerInput {

        private UUID customerId;
        private PageRequest pageRequest;

        @Override
        public PageResult<OrderQueryPort> execute(UUID customerId, PageRequest pageRequest) {
            this.customerId = customerId;
            this.pageRequest = pageRequest;
            List<OrderQueryPort> orders = List.of(
                    new OrderQueryPort(
                            UUID.randomUUID(),
                            customerId,
                            UUID.randomUUID(),
                            new BigDecimal("30.00"),
                            OrderStatus.CREATED,
                            List.of(new OrderItemQueryPort(UUID.randomUUID(), "Burger", 2, new BigDecimal("15.00")))
                    )
            );
            return PageResult.of(orders, pageRequest.page(), pageRequest.size(), orders.size());
        }
    }
}
