package br.com.fiap.cheffy.infrastructure.external_client.order_confirmation_service;

import br.com.fiap.cheffy.infrastructure.external_client.order_confirmation_service.config.OrderConfirmationFeignConfig;
import br.com.fiap.cheffy.infrastructure.external_client.order_confirmation_service.dto.OrderConfirmationRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "order-confirmation-service",
        url = "${order.service.url}",
        configuration = OrderConfirmationFeignConfig.class
)
public interface OrderConfirmationFeignClient {

    @PostMapping("/orders/confirmations")
    void confirm(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody OrderConfirmationRequestDto request
    );
}
