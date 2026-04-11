package br.com.fiap.cheffy.application.fooditem.service;

import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.fooditem.exception.FoodItemNotFoundException;
import br.com.fiap.cheffy.domain.fooditem.port.output.FoodItemRepository;
import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantNotFoundException;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantOperationNotAllowedException;
import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoodItemServiceHelperTest {

    @Mock
    private FoodItemRepository foodItemRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    private FoodItemServiceHelper helper;

    @BeforeEach
    void setUp() {
        helper = new FoodItemServiceHelper(foodItemRepository, restaurantRepository);
    }

    @Test
    void getActiveRestaurantOrFailReturnsRestaurantWhenActiveAndFound() {
        UUID restaurantId = UUID.randomUUID();
        Restaurant restaurant = mock(Restaurant.class);
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurant.isActive()).thenReturn(true);

        Restaurant result = helper.getActiveRestaurantOrFail(restaurantId);

        assertThat(result).isEqualTo(restaurant);
    }

    @Test
    void getActiveRestaurantOrFailThrowsWhenNotFound() {
        UUID restaurantId = UUID.randomUUID();
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class, () -> helper.getActiveRestaurantOrFail(restaurantId));
    }

    @Test
    void getActiveRestaurantOrFailThrowsWhenInactive() {
        UUID restaurantId = UUID.randomUUID();
        Restaurant restaurant = mock(Restaurant.class);
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurant.isActive()).thenReturn(false);

        assertThrows(RestaurantOperationNotAllowedException.class, () -> helper.getActiveRestaurantOrFail(restaurantId));
    }

    @Test
    void getFoodItemOrFailReturnsFoodItemWhenFound() {
        UUID foodItemId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        FoodItem foodItem = mock(FoodItem.class);
        when(foodItemRepository.findByIdAndRestaurantId(foodItemId, restaurantId)).thenReturn(Optional.of(foodItem));

        FoodItem result = helper.getFoodItemOrFail(foodItemId, restaurantId);

        assertThat(result).isEqualTo(foodItem);
    }

    @Test
    void getFoodItemOrFailThrowsWhenNotFound() {
        UUID foodItemId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        when(foodItemRepository.findByIdAndRestaurantId(foodItemId, restaurantId)).thenReturn(Optional.empty());

        assertThrows(FoodItemNotFoundException.class, () -> helper.getFoodItemOrFail(foodItemId, restaurantId));
    }

    @Test
    void saveFoodItemDelegatesToRepository() {
        FoodItem foodItem = mock(FoodItem.class);

        helper.saveFoodItem(foodItem);

        verify(foodItemRepository).save(foodItem);
    }
}
