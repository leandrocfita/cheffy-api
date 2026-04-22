package br.com.fiap.cheffy.infrastructure.bean_config;

import br.com.fiap.cheffy.application.fooditem.service.FoodItemServiceHelper;
import br.com.fiap.cheffy.application.order.mapper.OrderQueryMapper;
import br.com.fiap.cheffy.application.order.usecase.CreateOrderUseCase;
import br.com.fiap.cheffy.application.order.usecase.FindOrderByIdUseCase;
import br.com.fiap.cheffy.application.order.usecase.ListOrdersByCustomerUseCase;
import br.com.fiap.cheffy.domain.order.port.output.OrderRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderUseCaseConfig {

    @Bean
    public CreateOrderUseCase createOrderUseCase(
            OrderRepository orderRepository,
            FoodItemServiceHelper foodItemServiceHelper
    ) {
        return new CreateOrderUseCase(orderRepository, foodItemServiceHelper);
    }

    @Bean
    public FindOrderByIdUseCase findOrderByIdUseCase(
            OrderRepository orderRepository,
            OrderQueryMapper orderQueryMapper
    ) {
        return new FindOrderByIdUseCase(orderRepository, orderQueryMapper);
    }

    @Bean
    public ListOrdersByCustomerUseCase listOrdersByCustomerUseCase(
            OrderRepository orderRepository,
            OrderQueryMapper orderQueryMapper
    ) {
        return new ListOrdersByCustomerUseCase(orderRepository, orderQueryMapper);
    }
}
