package br.com.fiap.cheffy.infrastructure.external_client.auth_service.config;

import br.com.fiap.cheffy.infrastructure.exception.ClientUnavailableException;
import br.com.fiap.cheffy.infrastructure.exception.LoginAlreadyExistsException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class AuthServiceErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {

        return switch (response.status()) {

            case 409 -> new LoginAlreadyExistsException();

            case 404 -> new RuntimeException("User not found");

            case 500, 503 -> new ClientUnavailableException();

            default -> defaultDecoder.decode(methodKey, response);
        };
    }
}
