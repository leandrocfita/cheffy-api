package br.com.fiap.cheffy.domain.fooditem.exception;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.*;
import static org.assertj.core.api.Assertions.assertThat;

class FoodItemExceptionTest {

    @Test
    void foodItemNotFoundExceptionHasCorrectMessage() {
        FoodItemNotFoundException ex = new FoodItemNotFoundException(FOOD_ITEM_NOT_FOUND, UUID.randomUUID());
        assertThat(ex.getMessage()).contains("FOOD_ITEM_NOT_FOUND");
    }

    @Test
    void foodItemOperationNotAllowedExceptionHasCorrectMessage() {
        FoodItemOperationNotAllowedException ex = new FoodItemOperationNotAllowedException(FOOD_ITEM_IS_ALREADY_INACTIVE);
        assertThat(ex.getMessage()).contains("FOOD_ITEM_IS_ALREADY_INACTIVE");
    }
}
