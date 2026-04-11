package br.com.fiap.cheffy.infrastructure.bean_config;

import br.com.fiap.cheffy.application.address.mapper.AddressQueryMapper;
import br.com.fiap.cheffy.application.fooditem.mapper.FoodItemQueryMapper;
import br.com.fiap.cheffy.application.profile.mapper.ProfileQueryMapper;
import br.com.fiap.cheffy.application.restaurant.mapper.ResturantQueryMapper;
import br.com.fiap.cheffy.application.user.mapper.UserQueryMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationMapperConfig {

    @Bean
    public UserQueryMapper userQueryMapper(
            AddressQueryMapper addressQueryMapper
    ) {
        return new UserQueryMapper(addressQueryMapper);
    }

    @Bean
    public ProfileQueryMapper profileQueryMapper() {
        return new ProfileQueryMapper();
    }

    @Bean
    public AddressQueryMapper addressQueryMapper() {
        return new AddressQueryMapper();
    }

    @Bean
    public FoodItemQueryMapper foodItemQueryMapper() {
        return new FoodItemQueryMapper();
    }

    @Bean
    public ResturantQueryMapper resturantQueryMapper(
            AddressQueryMapper addressQueryMapper,
            FoodItemQueryMapper foodItemQueryMapper) {
        return new ResturantQueryMapper(addressQueryMapper, foodItemQueryMapper);
    }
}
