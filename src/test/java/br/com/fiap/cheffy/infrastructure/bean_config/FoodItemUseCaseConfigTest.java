package br.com.fiap.cheffy.infrastructure.bean_config;

import br.com.fiap.cheffy.application.fooditem.mapper.FoodItemQueryMapper;
import br.com.fiap.cheffy.application.fooditem.service.FoodItemServiceHelper;
import br.com.fiap.cheffy.application.fooditem.usecase.DeactivateFoodItemUseCase;
import br.com.fiap.cheffy.application.fooditem.usecase.ReactivateFoodItemUseCase;
import br.com.fiap.cheffy.application.fooditem.usecase.UpdateFoodItemAvailabilityUseCase;
import br.com.fiap.cheffy.application.fooditem.usecase.CreateFoodItemUseCase;
import br.com.fiap.cheffy.application.fooditem.usecase.ListFoodItemsByRestaurantUseCase;
import br.com.fiap.cheffy.application.restaurant.service.RestaurantServiceHelper;
import br.com.fiap.cheffy.application.fooditem.usecase.FindFoodItemByIdUseCase;
import br.com.fiap.cheffy.domain.fooditem.port.output.FoodItemRepository;
import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FoodItemUseCaseConfigTest {

    @Mock
    private FoodItemRepository foodItemRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantServiceHelper restaurantServiceHelper;

    @Mock
    private FoodItemQueryMapper mapper;

    @Mock
    private FoodItemServiceHelper foodItemServiceHelper;

    @Test
    void foodItemServiceHelperCreatesBean() {
        FoodItemUseCaseConfig config = new FoodItemUseCaseConfig();
        assertThat(config.foodItemServiceHelper(foodItemRepository, restaurantRepository)).isNotNull();
    }

    @Test
    void createFoodItemUseCaseCreatesBean() {
        FoodItemUseCaseConfig config = new FoodItemUseCaseConfig();

        CreateFoodItemUseCase useCase = config.createFoodItemUseCase(foodItemRepository, restaurantRepository);

        assertThat(useCase).isNotNull();
    }

    @Test
    void listFoodItemsByRestaurantUseCaseCreatesBean() {
        FoodItemUseCaseConfig config = new FoodItemUseCaseConfig();

        ListFoodItemsByRestaurantUseCase useCase = config.listFoodItemsByRestaurantUseCase(foodItemRepository, restaurantServiceHelper, mapper);

        assertThat(useCase).isNotNull();
    }

    @Test
    void findFoodItemByIdUseCaseCreatesBean() {
        FoodItemUseCaseConfig config = new FoodItemUseCaseConfig();
        FindFoodItemByIdUseCase useCase = config.findFoodItemByIdUseCase(foodItemRepository, mapper);
        assertThat(useCase).isNotNull();
    }

    @Test
    void deactivateFoodItemUseCaseCreatesBean() {
        FoodItemUseCaseConfig config = new FoodItemUseCaseConfig();
        DeactivateFoodItemUseCase useCase = config.deactivateFoodItemUseCase(foodItemServiceHelper);
        assertThat(useCase).isNotNull();
    }

    @Test
    void reactivateFoodItemUseCaseCreatesBean() {
        FoodItemUseCaseConfig config = new FoodItemUseCaseConfig();
        ReactivateFoodItemUseCase useCase = config.reactivateFoodItemUseCase(foodItemServiceHelper);
        assertThat(useCase).isNotNull();
    }

    @Test
    void updateFoodItemAvailabilityUseCaseCreatesBean() {
        FoodItemUseCaseConfig config = new FoodItemUseCaseConfig();
        UpdateFoodItemAvailabilityUseCase useCase = config.updateFoodItemAvailabilityUseCase(foodItemServiceHelper);
        assertThat(useCase).isNotNull();
    }
}
