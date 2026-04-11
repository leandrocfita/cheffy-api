package br.com.fiap.cheffy.infrastructure.bean_config;

import br.com.fiap.cheffy.application.fooditem.service.FoodItemServiceHelper;
import br.com.fiap.cheffy.application.fooditem.mapper.FoodItemQueryMapper;
import br.com.fiap.cheffy.application.fooditem.usecase.CreateFoodItemUseCase;
import br.com.fiap.cheffy.application.fooditem.usecase.DeactivateFoodItemUseCase;
import br.com.fiap.cheffy.application.fooditem.usecase.ReactivateFoodItemUseCase;
import br.com.fiap.cheffy.application.fooditem.usecase.UpdateFoodItemAvailabilityUseCase;
import br.com.fiap.cheffy.application.fooditem.usecase.UpdateFoodItemUseCase;
import br.com.fiap.cheffy.application.fooditem.usecase.ListFoodItemsByRestaurantUseCase;
import br.com.fiap.cheffy.application.restaurant.service.RestaurantServiceHelper;
import br.com.fiap.cheffy.application.fooditem.usecase.FindFoodItemByIdUseCase;
import br.com.fiap.cheffy.domain.fooditem.port.output.FoodItemRepository;
import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FoodItemUseCaseConfig {

    @Bean
    FoodItemServiceHelper foodItemServiceHelper(FoodItemRepository foodItemRepository, RestaurantRepository restaurantRepository) {
        return new FoodItemServiceHelper(foodItemRepository, restaurantRepository);
    }

    @Bean
    CreateFoodItemUseCase createFoodItemUseCase(FoodItemRepository foodItemRepository, RestaurantRepository restaurantRepository) {
        return new CreateFoodItemUseCase(foodItemRepository, restaurantRepository);
    }

    @Bean
    UpdateFoodItemUseCase updateFoodItemUseCase(FoodItemRepository foodItemRepository, RestaurantRepository restaurantRepository){
        return new UpdateFoodItemUseCase(foodItemRepository, restaurantRepository);
    }

    @Bean
    ListFoodItemsByRestaurantUseCase listFoodItemsByRestaurantUseCase(
            FoodItemRepository foodItemRepository,
            RestaurantServiceHelper restaurantServiceHelper,
            FoodItemQueryMapper foodItemQueryMapper)
    {
        return new ListFoodItemsByRestaurantUseCase(foodItemRepository, restaurantServiceHelper, foodItemQueryMapper);
    }

    @Bean
    FindFoodItemByIdUseCase findFoodItemByIdUseCase(
            FoodItemRepository foodItemRepository,
            FoodItemQueryMapper foodItemQueryMapper
    ) {
        return new FindFoodItemByIdUseCase(foodItemRepository, foodItemQueryMapper);
    }

    @Bean
    DeactivateFoodItemUseCase deactivateFoodItemUseCase(FoodItemServiceHelper foodItemServiceHelper) {
        return new DeactivateFoodItemUseCase(foodItemServiceHelper);
    }

    @Bean
    ReactivateFoodItemUseCase reactivateFoodItemUseCase(FoodItemServiceHelper foodItemServiceHelper) {
        return new ReactivateFoodItemUseCase(foodItemServiceHelper);
    }

    @Bean
    UpdateFoodItemAvailabilityUseCase updateFoodItemAvailabilityUseCase(FoodItemServiceHelper foodItemServiceHelper) {
        return new UpdateFoodItemAvailabilityUseCase(foodItemServiceHelper);
    }
}
