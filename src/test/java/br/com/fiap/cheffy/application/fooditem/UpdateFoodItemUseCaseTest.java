//package br.com.fiap.cheffy.application.fooditem;
//
//import br.com.fiap.cheffy.application.fooditem.dto.FoodItemCommandPort;
//import br.com.fiap.cheffy.application.fooditem.usecase.UpdateFoodItemUseCase;
//import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
//import br.com.fiap.cheffy.domain.fooditem.port.output.FoodItemRepository;
//import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
//import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantNotFoundException;
//import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;
//import br.com.fiap.cheffy.utils.FoodItemTestUtils;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class UpdateFoodItemUseCaseTest {
//
//    @Mock
//    private FoodItemRepository foodItemRepository;
//
//    @Mock
//    private RestaurantRepository restaurantRepository;
//
//    @InjectMocks
//    private UpdateFoodItemUseCase updateFoodItemUseCase;
//
//    @Test
//    @DisplayName("Should update food item successfully when all validations pass")
//    void shouldUpdateFoodItemSuccessfully() {
//        // Given
//        UUID restaurantId = UUID.randomUUID();
//        UUID foodItemId = UUID.randomUUID();
//        UUID userId = UUID.randomUUID();
//
//        FoodItemCommandPort foodItemCommandPort = new FoodItemCommandPort("Novo Nome", "Nova Desc", BigDecimal.TEN, "url", restaurantId, true, true, true);
//
//        Restaurant restaurant = mock(Restaurant.class);
//
//        FoodItem foodItem = mock(FoodItem.class);
//
//
//        when(foodItem.getId()).thenReturn(foodItemId);
//        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
//        when(foodItemRepository.findById(foodItemId)).thenReturn(Optional.of(foodItem));
//        when(foodItemRepository.existsInRestaurantById(restaurantId,foodItemId)).thenReturn(true);
//        when(restaurant.isOwnedByUser(userId)).thenReturn(Boolean.TRUE);
//
//        // When
//        updateFoodItemUseCase.update(foodItemId, restaurantId, userId, foodItemCommandPort);
//
//        // Then
//        verify(foodItemRepository, times(1)).save(foodItem);
//        // Verifies that the patch method was called to update the entity attributes
//        verify(foodItem, times(1)).patch(any(FoodItem.class));
//    }
//
//    @Test
//    @DisplayName("Should throw exception when restaurant does not exist")
//    void shouldThrowExceptionWhenRestaurantDoesNotExist() {
//        // Given
//        UUID restaurantId = UUID.randomUUID();
//        UUID foodItemId = UUID.randomUUID();
//        UUID userId = UUID.randomUUID();
//
//        FoodItemCommandPort foodItemCommandPort = mock(FoodItemCommandPort.class);
//        FoodItem originalFoodItem = FoodItemTestUtils.createTestFoodItemDomainEntity();
//
//        when(foodItemRepository.findById(foodItemId)).thenReturn(Optional.of(originalFoodItem));
//        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());
//
//        // When & Then
//        assertThrows(RestaurantNotFoundException.class, () ->
//                updateFoodItemUseCase.update(foodItemId, restaurantId, userId, foodItemCommandPort));
//
//        verify(foodItemRepository, never()).save(any());
//    }
//
//    @Test
//    @DisplayName("Should throw exception when food item does not exist")
//    void shouldThrowExceptionWhenFoodItemDoesNotExist() {
//        // Given
//        UUID restaurantId = UUID.randomUUID();
//        UUID foodItemId = UUID.randomUUID();
//        UUID userId = UUID.randomUUID();
//        FoodItemCommandPort foodItemCommandPort = mock(FoodItemCommandPort.class);
//
//        when(foodItemRepository.findById(foodItemId)).thenReturn(Optional.empty());
//
//        // When & Then
//        assertThrows(RuntimeException.class, () -> // Replace with FoodItemNotFoundException.class if you have it
//                updateFoodItemUseCase.update(foodItemId, restaurantId, userId, foodItemCommandPort));
//
//        verify(foodItemRepository, never()).save(any());
//    }
//}
