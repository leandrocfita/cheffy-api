package br.com.fiap.cheffy.infrastructure.external_client.auth_service;

import br.com.fiap.cheffy.application.user.dto.AuthUserCommandPort;
import br.com.fiap.cheffy.infrastructure.external_client.auth_service.config.AuthFeignConfig;
import br.com.fiap.cheffy.infrastructure.external_client.auth_service.dto.CreateUserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "auth-service",
        url = "${auth.service.url}",
        configuration = AuthFeignConfig.class
)
public interface AuthServiceFeignClient {

    @PostMapping("/auth/register")
    CreateUserResponseDto createUser(@RequestBody AuthUserCommandPort request);
}
