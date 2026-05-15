package br.com.fiap.cheffy.infrastructure.external_client.order_confirmation_service.config;

import br.com.fiap.cheffy.infrastructure.exception.ClientUnavailableException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class OrderConfirmationServiceErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 503 -> new ClientUnavailableException();
            default -> defaultDecoder.decode(methodKey, response);
        };
    }
}
