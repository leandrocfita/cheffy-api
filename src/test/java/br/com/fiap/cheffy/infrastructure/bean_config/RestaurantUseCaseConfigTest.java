package br.com.fiap.cheffy.infrastructure.bean_config;

import br.com.fiap.cheffy.application.restaurant.mapper.ResturantQueryMapper;
import br.com.fiap.cheffy.application.restaurant.service.RestaurantServiceHelper;
import br.com.fiap.cheffy.application.restaurant.usecase.DeactivateRestaurantUseCase;
import br.com.fiap.cheffy.application.restaurant.usecase.FindRestaurantByIdUseCase;
import br.com.fiap.cheffy.application.restaurant.usecase.ReactivateRestaurantUseCase;
import br.com.fiap.cheffy.application.restaurant.usecase.RegisterRestaurantUseCase;
import br.com.fiap.cheffy.application.user.service.UserServiceHelper;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseConfigTest {

    @Mock
    private UserServiceHelper userServiceHelper;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private RestaurantServiceHelper restaurantServiceHelper;
    @Mock
    private ResturantQueryMapper resturantQueryMapper;

    @Test
    void restaurantServiceHelperCreatesBean() {
        RestaurantUseCaseConfig config = new RestaurantUseCaseConfig();

        RestaurantServiceHelper helper = config.restaurantServiceHelper(restaurantRepository);

        assertThat(helper).isNotNull();
    }

    @Test
    void registerRestarantUseCaseCreatesBean() {
        RestaurantUseCaseConfig config = new RestaurantUseCaseConfig();

        RegisterRestaurantUseCase useCase = config.registerRestarantUseCase(
                userServiceHelper,
                restaurantRepository,
                profileRepository,
                restaurantServiceHelper
        );

        assertThat(useCase).isNotNull();
    }

    @Test
    void deactivateRestaurantUseCaseCreatesBean() {
        RestaurantUseCaseConfig config = new RestaurantUseCaseConfig();

        DeactivateRestaurantUseCase useCase = config.deactivateRestaurantUseCase(restaurantServiceHelper);

        assertThat(useCase).isNotNull();
    }

    @Test
    void reactivateRestaurantUseCaseCreatesBean() {
        RestaurantUseCaseConfig config = new RestaurantUseCaseConfig();

        ReactivateRestaurantUseCase useCase = config.reactivateRestaurantUseCase(restaurantServiceHelper);

        assertThat(useCase).isNotNull();
    }

    @Test
    void findRestaurantByIdUseCaseCreatesBean() {
        RestaurantUseCaseConfig config = new RestaurantUseCaseConfig();

        FindRestaurantByIdUseCase useCase = config.findRestaurantByIdUseCase(restaurantServiceHelper, resturantQueryMapper);

        assertThat(useCase).isNotNull();
    }
}
