package br.com.fiap.cheffy.domain.fooditem.exception;

import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FoodItemUnavailableForOrderExceptionTest {

    @Test
    void shouldExposeFoodItemId() {
        UUID foodItemId = UUID.randomUUID();

        FoodItemUnavailableForOrderException exception =
                new FoodItemUnavailableForOrderException(ExceptionsKeys.FOOD_ITEM_UNAVAILABLE_FOR_ORDER, foodItemId);

        assertThat(exception.getId()).isEqualTo(foodItemId);
        assertThat(exception.getMessage()).isEqualTo(ExceptionsKeys.FOOD_ITEM_UNAVAILABLE_FOR_ORDER.toString());
    }
}
