package br.com.fiap.cheffy.infrastructure.external_client.order_confirmation_service;

import br.com.fiap.cheffy.application.order.dto.OrderConfirmationCommandPort;
import br.com.fiap.cheffy.domain.order.port.output.OrderConfirmationExternalClient;
import br.com.fiap.cheffy.infrastructure.exception.ClientUnavailableException;
import br.com.fiap.cheffy.infrastructure.external_client.order_confirmation_service.dto.OrderConfirmationRequestDto;
import feign.RetryableException;
import org.springframework.stereotype.Service;

@Service
public class OrderConfirmationAdapter implements OrderConfirmationExternalClient {

    private final OrderConfirmationFeignClient orderConfirmationFeignClient;

    public OrderConfirmationAdapter(OrderConfirmationFeignClient orderConfirmationFeignClient) {
        this.orderConfirmationFeignClient = orderConfirmationFeignClient;
    }

    @Override
    public void confirm(OrderConfirmationCommandPort command) {
        try {
            orderConfirmationFeignClient.confirm(
                    command.authorizationHeader(),
                    new OrderConfirmationRequestDto(
                            command.orderId(),
                            command.totalAmount()
                    )
            );
        } catch (RetryableException ex) {
            throw new ClientUnavailableException();
        }
    }
}
