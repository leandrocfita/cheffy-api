package br.com.fiap.cheffy.application.fooditem;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemCommandPort;
import br.com.fiap.cheffy.application.fooditem.usecase.CreateFoodItemUseCase;
import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.fooditem.port.output.FoodItemRepository;
import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantDoesNotExistException;
import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class CreateFoodItemUseCaseTest {

    @Mock
    private FoodItemRepository foodItemRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private CreateFoodItemUseCase createFoodItemUseCase;

    @Test
    @DisplayName("Should create food item successfully when it does not exist")
    void shouldCreateFoodItemSuccessfully() {
        // Given
        UUID restaurantId = UUID.randomUUID();
        var input = new FoodItemCommandPort("X-Burger", "Delicious burger", BigDecimal.TEN, "http://img.com", restaurantId, false, false, false);
        Restaurant testRestaurant = createPersistentRestaurantEntity();

        //Simulate restaurant exists
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(testRestaurant));
        when(foodItemRepository.existsByNameIgnoreCaseAndRestaurantId(input.name(), restaurantId)).thenReturn(false);
        when(foodItemRepository.save(any(FoodItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        FoodItem result = createFoodItemUseCase.execute(input);

        // Then
        assertNotNull(result);
        assertEquals(input.name(), result.getName());
        verify(foodItemRepository, times(1)).save(any(FoodItem.class));
    }

    @Test
    @DisplayName("Should throw exception when food item already exists")
    void shouldThrowExceptionWhenFoodItemAlreadyExists() {
        // Given
        UUID restaurantId = UUID.randomUUID();
        FoodItemCommandPort input = new FoodItemCommandPort("X-Burger", "Delicious burger", BigDecimal.TEN, "http://img.com", restaurantId, false, false, false);

        Restaurant testRestaurant = createPersistentRestaurantEntity();

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(testRestaurant));
        when(foodItemRepository.existsByNameIgnoreCaseAndRestaurantId(input.name(), restaurantId)).thenReturn(true);

        // When & Then
        assertThrows(Exception.class, () -> createFoodItemUseCase.execute(input));
        verify(foodItemRepository, never()).save(any(FoodItem.class));
    }

    @Test
    @DisplayName("Should throw exception if a Restaurant was not found")
    void shouldThrowExceptionWhenRestaurantDoesNotExist(){
        UUID restaurantId = UUID.randomUUID();
        var input = new FoodItemCommandPort("X-Burger", "Delicious burger", BigDecimal.TEN, "http://img.com", restaurantId, false, false, false);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThrows(RestaurantDoesNotExistException.class, ()-> createFoodItemUseCase.execute(input));

        verify(foodItemRepository, never()).save(any(FoodItem.class));
        verify(restaurantRepository, times(1)).findById(restaurantId);

    }

    private Restaurant createPersistentRestaurantEntity(){

        return Restaurant.create24h("teste", "cnpj-teste", "culinaria-teste", ZoneId.systemDefault(), null);
    }
}
