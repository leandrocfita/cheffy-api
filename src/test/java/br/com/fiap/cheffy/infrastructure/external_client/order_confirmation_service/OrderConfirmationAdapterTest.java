package br.com.fiap.cheffy.infrastructure.external_client.order_confirmation_service;

import br.com.fiap.cheffy.application.order.dto.OrderConfirmationCommandPort;
import br.com.fiap.cheffy.infrastructure.exception.ClientUnavailableException;
import br.com.fiap.cheffy.infrastructure.external_client.order_confirmation_service.dto.OrderConfirmationRequestDto;
import feign.Request;
import feign.RetryableException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderConfirmationAdapterTest {

    private static final String AUTHORIZATION_HEADER = "Bearer user-token";

    @Test
    void confirmMapsCommandToFeignRequest() {
        StubOrderConfirmationFeignClient feignClient = new StubOrderConfirmationFeignClient();
        OrderConfirmationAdapter adapter = new OrderConfirmationAdapter(feignClient);
        UUID orderId = UUID.randomUUID();

        adapter.confirm(new OrderConfirmationCommandPort(orderId, new BigDecimal("30.00"), AUTHORIZATION_HEADER));

        assertThat(feignClient.lastAuthorizationHeader).isEqualTo(AUTHORIZATION_HEADER);
        assertThat(feignClient.lastRequest.orderId()).isEqualTo(orderId);
        assertThat(feignClient.lastRequest.totalAmount()).isEqualByComparingTo("30.00");
    }

    @Test
    void confirmThrowsClientUnavailableWhenFeignCannotConnect() {
        OrderConfirmationAdapter adapter = new OrderConfirmationAdapter(new UnavailableOrderConfirmationFeignClient());
        UUID orderId = UUID.randomUUID();

        assertThatThrownBy(() -> adapter.confirm(new OrderConfirmationCommandPort(
                orderId,
                new BigDecimal("30.00"),
                AUTHORIZATION_HEADER
        ))).isInstanceOf(ClientUnavailableException.class);
    }

    private static class StubOrderConfirmationFeignClient implements OrderConfirmationFeignClient {

        private OrderConfirmationRequestDto lastRequest;
        private String lastAuthorizationHeader;

        @Override
        public void confirm(String authorizationHeader, OrderConfirmationRequestDto request) {
            this.lastAuthorizationHeader = authorizationHeader;
            this.lastRequest = request;
        }
    }

    private static class UnavailableOrderConfirmationFeignClient implements OrderConfirmationFeignClient {

        @Override
        public void confirm(String authorizationHeader, OrderConfirmationRequestDto request) {
            throw new RetryableException(
                    503,
                    "Connection refused",
                    Request.HttpMethod.POST,
                    (Long) null,
                    Request.create(
                            Request.HttpMethod.POST,
                            "http://localhost:8083/orders/confirmations",
                            Collections.emptyMap(),
                            new byte[0],
                            StandardCharsets.UTF_8
                    )
            );
        }
    }
}
