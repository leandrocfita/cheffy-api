package br.com.fiap.cheffy.application.fooditem.usecase;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemAvailabilityCommandPort;
import br.com.fiap.cheffy.application.fooditem.service.FoodItemServiceHelper;
import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.fooditem.exception.FoodItemOperationNotAllowedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateFoodItemAvailabilityUseCaseTest {

    @Mock
    private FoodItemServiceHelper foodItemServiceHelper;

    private UpdateFoodItemAvailabilityUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateFoodItemAvailabilityUseCase(foodItemServiceHelper);
    }

    @Test
    void executeUpdatesAvailabilityWhenFoodItemIsActive() {
        UUID restaurantId = UUID.randomUUID();
        UUID foodItemId = UUID.randomUUID();
        FoodItem foodItem = mock(FoodItem.class);
        FoodItemAvailabilityCommandPort command = new FoodItemAvailabilityCommandPort(true, false);

        when(foodItemServiceHelper.getFoodItemOrFail(foodItemId, restaurantId)).thenReturn(foodItem);
        when(foodItem.isActive()).thenReturn(true);

        useCase.execute(restaurantId, foodItemId, command);

        verify(foodItemServiceHelper).getActiveRestaurantOrFail(restaurantId);
        verify(foodItem).updateAvailability(true, false);
        verify(foodItemServiceHelper).saveFoodItem(foodItem);
    }

    @Test
    void executeThrowsWhenFoodItemIsInactive() {
        UUID restaurantId = UUID.randomUUID();
        UUID foodItemId = UUID.randomUUID();
        FoodItem foodItem = mock(FoodItem.class);
        FoodItemAvailabilityCommandPort command = new FoodItemAvailabilityCommandPort(true, false);

        when(foodItemServiceHelper.getFoodItemOrFail(foodItemId, restaurantId)).thenReturn(foodItem);
        when(foodItem.isActive()).thenReturn(false);

        assertThrows(FoodItemOperationNotAllowedException.class, () -> useCase.execute(restaurantId, foodItemId, command));
        verify(foodItemServiceHelper, never()).saveFoodItem(any());
    }

    @Test
    void executeThrowsWhenDeliveryEnabledButItemWillBeUnavailable() {
        UUID restaurantId = UUID.randomUUID();
        UUID foodItemId = UUID.randomUUID();
        FoodItem foodItem = mock(FoodItem.class);
        // available=false, deliveryAvailable=true → should throw
        FoodItemAvailabilityCommandPort command = new FoodItemAvailabilityCommandPort(false, true);

        when(foodItemServiceHelper.getFoodItemOrFail(foodItemId, restaurantId)).thenReturn(foodItem);
        when(foodItem.isActive()).thenReturn(true);

        assertThrows(FoodItemOperationNotAllowedException.class, () -> useCase.execute(restaurantId, foodItemId, command));
        verify(foodItemServiceHelper, never()).saveFoodItem(any());
    }

    @Test
    void executeThrowsWhenDeliveryEnabledAndAvailableIsNullButItemIsUnavailable() {
        UUID restaurantId = UUID.randomUUID();
        UUID foodItemId = UUID.randomUUID();
        FoodItem foodItem = mock(FoodItem.class);
        // available=null (keep current), deliveryAvailable=true, but item is currently unavailable
        FoodItemAvailabilityCommandPort command = new FoodItemAvailabilityCommandPort(null, true);

        when(foodItemServiceHelper.getFoodItemOrFail(foodItemId, restaurantId)).thenReturn(foodItem);
        when(foodItem.isActive()).thenReturn(true);
        when(foodItem.isAvailable()).thenReturn(false);

        assertThrows(FoodItemOperationNotAllowedException.class, () -> useCase.execute(restaurantId, foodItemId, command));
        verify(foodItemServiceHelper, never()).saveFoodItem(any());
    }

    @Test
    void executeUpdatesWhenAvailableIsNullAndDeliveryIsFalse() {
        UUID restaurantId = UUID.randomUUID();
        UUID foodItemId = UUID.randomUUID();
        FoodItem foodItem = mock(FoodItem.class);
        FoodItemAvailabilityCommandPort command = new FoodItemAvailabilityCommandPort(null, false);

        when(foodItemServiceHelper.getFoodItemOrFail(foodItemId, restaurantId)).thenReturn(foodItem);
        when(foodItem.isActive()).thenReturn(true);
        when(foodItem.isAvailable()).thenReturn(true);

        useCase.execute(restaurantId, foodItemId, command);

        verify(foodItem).updateAvailability(null, false);
        verify(foodItemServiceHelper).saveFoodItem(foodItem);
    }
}
