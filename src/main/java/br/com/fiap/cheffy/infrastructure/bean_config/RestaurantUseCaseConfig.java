package br.com.fiap.cheffy.infrastructure.bean_config;

import br.com.fiap.cheffy.application.restaurant.mapper.ResturantQueryMapper;
import br.com.fiap.cheffy.application.restaurant.service.RestaurantServiceHelper;
import br.com.fiap.cheffy.application.restaurant.usecase.*;
import br.com.fiap.cheffy.application.user.service.UserServiceHelper;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestaurantUseCaseConfig {

    @Bean
    public RestaurantServiceHelper restaurantServiceHelper(
            RestaurantRepository restaurantRepository
    ) {
        return new RestaurantServiceHelper(restaurantRepository);
    }

    @Bean
    public RegisterRestaurantUseCase registerRestarantUseCase(
            UserServiceHelper userServiceHelper,
            RestaurantRepository restaurantRepository,
            ProfileRepository profileRepository,
            RestaurantServiceHelper restaurantServiceHelper
    ) {
        return new RegisterRestaurantUseCase(
                userServiceHelper,
                restaurantRepository,
                profileRepository,
                restaurantServiceHelper
        );

    }

    @Bean
    public DeactivateRestaurantUseCase deactivateRestaurantUseCase(
            RestaurantServiceHelper restaurantServiceHelper
    ) {
        return new DeactivateRestaurantUseCase(
                restaurantServiceHelper
        );
    }

    @Bean
    public ReactivateRestaurantUseCase reactivateRestaurantUseCase(
            RestaurantServiceHelper restaurantServiceHelper
    ) {
        return new ReactivateRestaurantUseCase(
                restaurantServiceHelper
        );
    }

    @Bean
    public UpdateRestaurantUseCase updateRestaurantUseCase(RestaurantServiceHelper restaurantServiceHelper) {
        return new UpdateRestaurantUseCase(restaurantServiceHelper);
    }

    @Bean
    public FindRestaurantByIdUseCase findRestaurantByIdUseCase(
            RestaurantServiceHelper restaurantServiceHelper,
            ResturantQueryMapper resturantQueryMapper
    ){
        return new FindRestaurantByIdUseCase(restaurantServiceHelper, resturantQueryMapper);
    }

}
