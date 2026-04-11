package br.com.fiap.cheffy.application.restaurant.service;

import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantNotFoundException;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantOperationNotAllowedException;
import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;
import br.com.fiap.cheffy.shared.exception.InvalidDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceHelperTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    private RestaurantServiceHelper helper;

    @BeforeEach
    void setUp() {
        helper = new RestaurantServiceHelper(restaurantRepository);
    }

    @Test
    void getRestaurantOrFailReturnsRestaurantWhenFound() {
        UUID id = UUID.randomUUID();
        Restaurant restaurant = mock(Restaurant.class);
        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));

        Restaurant result = helper.getRestaurantOrFail(id);

        assertThat(result).isEqualTo(restaurant);
        verify(restaurantRepository).findById(id);
    }

    @Test
    void getRestaurantOrFailThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(restaurantRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class, () -> helper.getRestaurantOrFail(id));
    }

    @Test
    void saveRestaurantDelegatesToRepository() {
        Restaurant restaurant = mock(Restaurant.class);
        Restaurant saved = mock(Restaurant.class);
        when(restaurantRepository.save(restaurant)).thenReturn(saved);

        Restaurant result = helper.saveRestaurant(restaurant);

        assertThat(result).isEqualTo(saved);
        verify(restaurantRepository).save(restaurant);
    }

    @Test
    void getRestaurantOrFailValidatingOwnershipReturnsRestaurantWhenOwned() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Restaurant restaurant = mock(Restaurant.class);
        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
        when(restaurant.isOwnedByUser(userId)).thenReturn(true);

        Restaurant foundRestaurant = helper.getRestaurantOrFailValidatingOwnership(id, userId);

        assertThat(foundRestaurant).isEqualTo(restaurant);
    }

    @Test
    void getRestaurantOrFailValidatingOwnershipThrowsWhenNotOwned() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Restaurant restaurant = mock(Restaurant.class);
        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
        when(restaurant.isOwnedByUser(userId)).thenReturn(false);

        assertThrows(RestaurantOperationNotAllowedException.class,
                () -> helper.getRestaurantOrFailValidatingOwnership(id, userId));
    }

    @Test
    void getRestaurantOrFailValidatingOwnershipThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(restaurantRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class,
                () -> helper.getRestaurantOrFailValidatingOwnership(id, userId));
    }

    @Test
    void extractZoneIdReturnsZoneIdWhenValid() {
        ZoneId extractedZoneId = helper.extractZoneId("America/Sao_Paulo");
        assertThat(extractedZoneId).isEqualTo(ZoneId.of("America/Sao_Paulo"));
    }

    @Test
    void extractZoneIdReturnsNullWhenInputIsNull() {
        ZoneId extractedZoneId = helper.extractZoneId(null);
        assertNull(extractedZoneId);
    }

    @Test
    void extractZoneIdThrowsWhenInvalid() {
        assertThrows(InvalidDataException.class, () -> helper.extractZoneId("Invalid/Zone"));
    }
}
