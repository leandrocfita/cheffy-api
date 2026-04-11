package br.com.fiap.cheffy.application.fooditem.usecase;

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
class DeactivateFoodItemUseCaseTest {

    @Mock
    private FoodItemServiceHelper foodItemServiceHelper;

    private DeactivateFoodItemUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeactivateFoodItemUseCase(foodItemServiceHelper);
    }

    @Test
    void executeDeactivatesFoodItemWhenActive() {
        UUID restaurantId = UUID.randomUUID();
        UUID foodItemId = UUID.randomUUID();
        FoodItem foodItem = mock(FoodItem.class);

        when(foodItemServiceHelper.getFoodItemOrFail(foodItemId, restaurantId)).thenReturn(foodItem);
        when(foodItem.isActive()).thenReturn(true);

        useCase.execute(restaurantId, foodItemId);

        verify(foodItemServiceHelper).getActiveRestaurantOrFail(restaurantId);
        verify(foodItem).deactivate();
        verify(foodItemServiceHelper).saveFoodItem(foodItem);
    }

    @Test
    void executeThrowsWhenFoodItemAlreadyInactive() {
        UUID restaurantId = UUID.randomUUID();
        UUID foodItemId = UUID.randomUUID();
        FoodItem foodItem = mock(FoodItem.class);

        when(foodItemServiceHelper.getFoodItemOrFail(foodItemId, restaurantId)).thenReturn(foodItem);
        when(foodItem.isActive()).thenReturn(false);

        assertThrows(FoodItemOperationNotAllowedException.class, () -> useCase.execute(restaurantId, foodItemId));
        verify(foodItemServiceHelper, never()).saveFoodItem(any());
    }
}
