package br.com.fiap.cheffy.application.restaurant.usecase;

import br.com.fiap.cheffy.application.restaurant.dto.UpdateRestaurantCommandPort;
import br.com.fiap.cheffy.application.restaurant.service.RestaurantServiceHelper;
import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantNotFoundException;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantOperationNotAllowedException;
import br.com.fiap.cheffy.shared.exception.InvalidDataException;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateRestaurantUseCaseTest {

    @Mock
    private RestaurantServiceHelper restaurantServiceHelper;
    private UpdateRestaurantUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateRestaurantUseCase(restaurantServiceHelper);
    }

    @Test
    void executeUpdatesRestaurantWhenOwnerIsValid() {
        UUID restaurantId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Restaurant restaurant = mock(Restaurant.class);
        UpdateRestaurantCommandPort command = new UpdateRestaurantCommandPort(
                "Novo Nome", "Japonesa", null, null, null, null
        );
        when(restaurantServiceHelper.getRestaurantOrFailValidatingOwnership(restaurantId, userId)).thenReturn(restaurant);
        useCase.execute(restaurantId, userId, command);
        verify(restaurant).patch(eq("Novo Nome"), eq("Japonesa"), isNull(), isNull());
        verify(restaurantServiceHelper).saveRestaurant(restaurant);
    }

    @Test
    void executeThrowsWhenRestaurantNotFound() {
        UUID restaurantId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UpdateRestaurantCommandPort command = new UpdateRestaurantCommandPort(
                "Nome", null, null, null, null, null
        );
        when(restaurantServiceHelper.getRestaurantOrFailValidatingOwnership(restaurantId, userId))
                .thenThrow(new RestaurantNotFoundException(
                        ExceptionsKeys.RESTAURANT_NOT_FOUND_EXCEPTION, restaurantId));
        assertThrows(RestaurantNotFoundException.class, () -> useCase.execute(restaurantId, userId, command));
        verify(restaurantServiceHelper, never()).saveRestaurant(any());
    }

    @Test
    void executeThrowsWhenUserDoesNotOwnRestaurant() {
        UUID restaurantId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UpdateRestaurantCommandPort command = new UpdateRestaurantCommandPort(
                "Nome", null, null, null, null, null
        );
        when(restaurantServiceHelper.getRestaurantOrFailValidatingOwnership(restaurantId, userId))
                .thenThrow(new RestaurantOperationNotAllowedException(
                        ExceptionsKeys.RESTAURANT_USER_DOES_NOT_HAVE_OWNERSHIP_OR_IS_INACTIVE));

        assertThrows(RestaurantOperationNotAllowedException.class, () -> useCase.execute(restaurantId, userId, command));

        verify(restaurantServiceHelper, never()).saveRestaurant(any());
    }

    @Test
    void executeThrowsWhenZoneIdIsInvalid() {
        UUID restaurantId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Restaurant restaurant = mock(Restaurant.class);
        UpdateRestaurantCommandPort command = new UpdateRestaurantCommandPort(
                null, null, null, null, "Invalid/Zone", null
        );
        when(restaurantServiceHelper.getRestaurantOrFailValidatingOwnership(restaurantId, userId)).thenReturn(restaurant);
        when(restaurantServiceHelper.extractZoneId("Invalid/Zone"))
                .thenThrow(new InvalidDataException(ExceptionsKeys.ZONE_ID_DO_NOT_EXIST));
        assertThrows(InvalidDataException.class, () -> useCase.execute(restaurantId, userId, command));
        verify(restaurant, never()).patch(any(), any(), any(), any());
        verify(restaurantServiceHelper, never()).saveRestaurant(any());
    }

    @Test
    void executeUpdatesWorkingHoursWhenProvided() {
        UUID restaurantId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Restaurant restaurant = mock(Restaurant.class);
        UpdateRestaurantCommandPort command = new UpdateRestaurantCommandPort(
                null, null, LocalTime.of(9, 0), LocalTime.of(18, 0), null, false
        );
        when(restaurantServiceHelper.getRestaurantOrFailValidatingOwnership(restaurantId, userId)).thenReturn(restaurant);
        useCase.execute(restaurantId, userId, command);
        verify(restaurant).patch(isNull(), isNull(), isNull(), notNull());
        verify(restaurantServiceHelper).saveRestaurant(restaurant);
    }

    @Test
    void executeUpdatesTo24hWhenOpen24hoursIsTrue() {
        UUID restaurantId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Restaurant restaurant = mock(Restaurant.class);
        UpdateRestaurantCommandPort command = new UpdateRestaurantCommandPort(
                null, null, null, null, null, true
        );
        when(restaurantServiceHelper.getRestaurantOrFailValidatingOwnership(restaurantId, userId)).thenReturn(restaurant);
        useCase.execute(restaurantId, userId, command);
        verify(restaurant).patch(isNull(), isNull(), isNull(), notNull());
        verify(restaurantServiceHelper).saveRestaurant(restaurant);
    }

    @Test
    void executeUpdatesWithValidZoneId() {
        UUID restaurantId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Restaurant restaurant = mock(Restaurant.class);
        UpdateRestaurantCommandPort command = new UpdateRestaurantCommandPort(
                null, null, null, null, "America/Sao_Paulo", null
        );
        when(restaurantServiceHelper.getRestaurantOrFailValidatingOwnership(restaurantId, userId)).thenReturn(restaurant);
        when(restaurantServiceHelper.extractZoneId("America/Sao_Paulo")).thenReturn(ZoneId.of("America/Sao_Paulo"));
        useCase.execute(restaurantId, userId, command);
        verify(restaurant).patch(isNull(), isNull(), notNull(), isNull());
        verify(restaurantServiceHelper).saveRestaurant(restaurant);
    }

    @Test
    void executeKeepsExistingWorkingHoursWhenOnlyOpeningTimeProvided() {
        UUID restaurantId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Restaurant restaurant = mock(Restaurant.class);
        UpdateRestaurantCommandPort command = new UpdateRestaurantCommandPort(
                null, null, LocalTime.of(8, 0), null, null, null
        );
        when(restaurantServiceHelper.getRestaurantOrFailValidatingOwnership(restaurantId, userId)).thenReturn(restaurant);
        when(restaurant.isOpen24hours()).thenReturn(false);
        when(restaurant.getClosingTime()).thenReturn(LocalTime.of(22, 0));
        useCase.execute(restaurantId, userId, command);
        verify(restaurant).patch(isNull(), isNull(), isNull(), notNull());
        verify(restaurantServiceHelper).saveRestaurant(restaurant);
    }
}