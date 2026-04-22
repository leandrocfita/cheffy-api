package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.application.order.dto.OrderCommandPort;
import br.com.fiap.cheffy.domain.order.port.input.CreateOrderInput;
import br.com.fiap.cheffy.presentation.dto.OrderCreateDTO;
import br.com.fiap.cheffy.presentation.dto.OrderItemDTO;
import br.com.fiap.cheffy.presentation.mapper.OrderWebMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class OrderControllerTest {

    @Test
    void createOrderReturnsCreatedWithOrderId() {
        InMemoryCreateOrderInput createOrderInput = new InMemoryCreateOrderInput();
        OrderController controller = new OrderController(createOrderInput, new OrderWebMapper());

        UUID userId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        UUID foodItemId = UUID.randomUUID();

        OrderCreateDTO dto = new OrderCreateDTO(
                restaurantId,
                List.of(new OrderItemDTO(foodItemId, "Burger", 2, new BigDecimal("15.00")))
        );

        ResponseEntity<String> response = controller.createOrder(dto, userId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(createOrderInput.createdId);
        assertThat(createOrderInput.customerId).isEqualTo(userId);
        assertThat(createOrderInput.command.restaurantId()).isEqualTo(restaurantId);
        assertThat(createOrderInput.command.items()).hasSize(1);
        assertThat(createOrderInput.command.items().getFirst().foodItemId()).isEqualTo(foodItemId);
    }

    private static class InMemoryCreateOrderInput implements CreateOrderInput {

        private final String createdId = UUID.randomUUID().toString();
        private OrderCommandPort command;
        private UUID customerId;

        @Override
        public String execute(OrderCommandPort command, UUID customerId) {
            this.command = command;
            this.customerId = customerId;
            return createdId;
        }
    }
}
