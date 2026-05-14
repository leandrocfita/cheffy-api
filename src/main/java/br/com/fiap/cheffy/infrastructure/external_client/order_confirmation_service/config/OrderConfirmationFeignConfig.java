package br.com.fiap.cheffy.infrastructure.external_client.order_confirmation_service.config;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfirmationFeignConfig {

    @Bean
    public ErrorDecoder orderConfirmationErrorDecoder() {
        return new OrderConfirmationServiceErrorDecoder();
    }
}
