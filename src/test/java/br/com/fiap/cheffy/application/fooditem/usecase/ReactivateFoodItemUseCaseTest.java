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
class ReactivateFoodItemUseCaseTest {

    @Mock
    private FoodItemServiceHelper foodItemServiceHelper;

    private ReactivateFoodItemUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ReactivateFoodItemUseCase(foodItemServiceHelper);
    }

    @Test
    void executeReactivatesFoodItemWhenInactive() {
        UUID restaurantId = UUID.randomUUID();
        UUID foodItemId = UUID.randomUUID();
        FoodItem foodItem = mock(FoodItem.class);

        when(foodItemServiceHelper.getFoodItemOrFail(foodItemId, restaurantId)).thenReturn(foodItem);
        when(foodItem.isActive()).thenReturn(false);

        useCase.execute(restaurantId, foodItemId);

        verify(foodItemServiceHelper).getActiveRestaurantOrFail(restaurantId);
        verify(foodItem).reactivate();
        verify(foodItemServiceHelper).saveFoodItem(foodItem);
    }

    @Test
    void executeThrowsWhenFoodItemAlreadyActive() {
        UUID restaurantId = UUID.randomUUID();
        UUID foodItemId = UUID.randomUUID();
        FoodItem foodItem = mock(FoodItem.class);

        when(foodItemServiceHelper.getFoodItemOrFail(foodItemId, restaurantId)).thenReturn(foodItem);
        when(foodItem.isActive()).thenReturn(true);

        assertThrows(FoodItemOperationNotAllowedException.class, () -> useCase.execute(restaurantId, foodItemId));
        verify(foodItemServiceHelper, never()).saveFoodItem(any());
    }
}
