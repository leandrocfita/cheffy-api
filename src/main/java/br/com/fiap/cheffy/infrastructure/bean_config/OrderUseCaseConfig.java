package br.com.fiap.cheffy.infrastructure.bean_config;

import br.com.fiap.cheffy.application.order.usecase.CreateOrderUseCase;
import br.com.fiap.cheffy.domain.order.port.output.OrderRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderUseCaseConfig {

    @Bean
    public CreateOrderUseCase createOrderUseCase(OrderRepository orderRepository) {
        return new CreateOrderUseCase(orderRepository);
    }
}
