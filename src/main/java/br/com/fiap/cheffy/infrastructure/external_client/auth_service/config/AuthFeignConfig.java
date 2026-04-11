package br.com.fiap.cheffy.infrastructure.external_client.auth_service.config;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthFeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new AuthServiceErrorDecoder();
    }
}
