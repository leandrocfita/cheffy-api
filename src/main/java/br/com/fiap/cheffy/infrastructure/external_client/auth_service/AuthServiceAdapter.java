package br.com.fiap.cheffy.infrastructure.external_client.auth_service;

import br.com.fiap.cheffy.application.user.dto.AuthUserCommandPort;
import br.com.fiap.cheffy.domain.user.port.output.AuthUserExternalClient;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceAdapter implements AuthUserExternalClient {

    private final AuthServiceFeignClient authServiceFeignClient;

    public AuthServiceAdapter(AuthServiceFeignClient authServiceFeignClient) {
        this.authServiceFeignClient = authServiceFeignClient;
    }

    @Override
    public String createUser(AuthUserCommandPort command) {
        return authServiceFeignClient.createUser(command).id();
    }
}
