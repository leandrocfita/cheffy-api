package br.com.fiap.cheffy.application.restaurant.usecase;

import br.com.fiap.cheffy.application.restaurant.service.RestaurantServiceHelper;
import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantNotFoundException;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantOperationNotAllowedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeactivateRestaurantUseCaseTest {

    @Mock
    private RestaurantServiceHelper restaurantServiceHelper;

    private DeactivateRestaurantUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeactivateRestaurantUseCase(restaurantServiceHelper);
    }

    @Test
    void executeDeactivatesRestaurantWhenOwnerIsValid() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Restaurant restaurant = mock(Restaurant.class);

        when(restaurantServiceHelper.getRestaurantOrFailValidatingOwnership(id, userId)).thenReturn(restaurant);

        useCase.execute(id, userId);

        verify(restaurant).deactivate();
        verify(restaurantServiceHelper).saveRestaurant(restaurant);
    }

    @Test
    void executeThrowsWhenRestaurantNotFound() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(restaurantServiceHelper.getRestaurantOrFailValidatingOwnership(id, userId))
                .thenThrow(new RestaurantNotFoundException(
                        br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.RESTAURANT_NOT_FOUND_EXCEPTION, id));

        assertThrows(RestaurantNotFoundException.class, () -> useCase.execute(id, userId));
        verify(restaurantServiceHelper, never()).saveRestaurant(any());
    }

    @Test
    void executeThrowsWhenUserDoesNotOwnRestaurant() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Restaurant restaurant = mock(Restaurant.class);

        when(restaurantServiceHelper.getRestaurantOrFailValidatingOwnership(id, userId))
                .thenThrow(new RestaurantOperationNotAllowedException(
                        br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.RESTAURANT_USER_DOES_NOT_HAVE_OWNERSHIP_OR_IS_INACTIVE));

        assertThrows(RestaurantOperationNotAllowedException.class, () -> useCase.execute(id, userId));
        verify(restaurantServiceHelper, never()).saveRestaurant(any());
    }
}
