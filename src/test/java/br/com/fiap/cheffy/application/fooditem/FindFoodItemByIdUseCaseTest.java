//package br.com.fiap.cheffy.application.fooditem;
//
//import br.com.fiap.cheffy.application.fooditem.dto.FoodItemQueryPort;
//import br.com.fiap.cheffy.application.fooditem.mapper.FoodItemQueryMapper;
//import br.com.fiap.cheffy.application.fooditem.usecase.FindFoodItemByIdUseCase;
//import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
//import br.com.fiap.cheffy.domain.fooditem.exception.FoodItemNotFoundException;
//import br.com.fiap.cheffy.domain.fooditem.port.output.FoodItemRepository;
//import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
//import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantInactiveException;
//import br.com.fiap.cheffy.presentation.mapper.FoodItemWebMapper;
//import br.com.fiap.cheffy.utils.FoodItemTestUtils;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class FindFoodItemByIdUseCaseTest {
//    @Mock
//    private FoodItemRepository foodItemRepository;
//    private FindFoodItemByIdUseCase useCase;
//    private FoodItemQueryMapper foodItemQueryMapper;
//
//    @BeforeEach
//    void setUp() {
//        foodItemQueryMapper = new FoodItemQueryMapper();
//        useCase = new FindFoodItemByIdUseCase(foodItemRepository, foodItemQueryMapper);
//    }
//
//    @Test
//    void shouldReturnFoodItemWhenFoundById() {
//        FoodItem foodItem = FoodItemTestUtils.createTestFoodItemDomainEntity();
//        UUID foodItemId = foodItem.getId();
//        UUID restaurantId = foodItem.getRestaurant().getId();
//        when(foodItemRepository.findByIdAndRestaurantId(foodItemId, restaurantId)).thenReturn(Optional.of(foodItem));
//        FoodItemQueryPort result = useCase.execute(restaurantId, foodItemId);
//        assertThat(result).isNotNull();
//        assertThat(result.id()).isEqualTo(foodItemId);
//        assertThat(result.name()).isEqualTo(foodItem.getName());
//        assertThat(result.description()).isEqualTo(foodItem.getDescription());
//        assertThat(result.price()).isEqualTo(foodItem.getPrice().value());
//        assertThat(result.restaurantId()).isEqualTo(restaurantId);
//        assertThat(result.deliveryAvailable()).isEqualTo(foodItem.isDeliveryAvailable());
//        assertThat(result.available()).isEqualTo(foodItem.isAvailable());
//        assertThat(result.active()).isEqualTo(foodItem.isActive());
//        verify(foodItemRepository, times(1)).findByIdAndRestaurantId(foodItemId, restaurantId);
//    }
//
//    @Test
//    void shouldThrowFoodItemNotFoundExceptionWhenNotFound() {
//        UUID foodItemId = UUID.randomUUID();
//        UUID restaurantId = UUID.randomUUID();
//        when(foodItemRepository.findByIdAndRestaurantId(foodItemId, restaurantId)).thenReturn(Optional.empty());
//        assertThatThrownBy(() -> useCase.execute(restaurantId, foodItemId))
//                .isInstanceOf(FoodItemNotFoundException.class);
//        verify(foodItemRepository, times(1)).findByIdAndRestaurantId(foodItemId, restaurantId);
//    }
//
//    @Test
//    void shouldThrowRestaurantInactiveExceptionWhenRestaurantIsInactive() {
//        FoodItem foodItem = FoodItemTestUtils.createTestFoodItemDomainEntity();
//        UUID foodItemId = foodItem.getId();
//        Restaurant restaurant = foodItem.getRestaurant();
//        UUID restaurantId = restaurant.getId();
//        restaurant.deactivate();
//        when(foodItemRepository.findByIdAndRestaurantId(foodItemId, restaurantId)).thenReturn(Optional.of(foodItem));
//        assertThatThrownBy(() -> useCase.execute(restaurantId, foodItemId))
//                .isInstanceOf(RestaurantInactiveException.class);
//        verify(foodItemRepository, times(1)).findByIdAndRestaurantId(foodItemId, restaurantId);
//    }
//}