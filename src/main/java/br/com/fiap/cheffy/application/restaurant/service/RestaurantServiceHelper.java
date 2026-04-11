package br.com.fiap.cheffy.application.restaurant.service;

import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantNotFoundException;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantOperationNotAllowedException;
import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;
import br.com.fiap.cheffy.shared.exception.InvalidDataException;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.UUID;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.*;

public class RestaurantServiceHelper {
    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceHelper(
            RestaurantRepository restaurantRepository
    ) {
        this.restaurantRepository = restaurantRepository;
    }

    public Restaurant getRestaurantOrFail(UUID id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(RESTAURANT_NOT_FOUND_EXCEPTION, id));
    }

    public Restaurant saveRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }


    public Restaurant getRestaurantOrFailValidatingOwnership(UUID id, UUID userId) {
        Restaurant restaurant = getRestaurantOrFail(id);
        if (!restaurant.isOwnedByUser(userId)) {
            throw new RestaurantOperationNotAllowedException(RESTAURANT_USER_DOES_NOT_HAVE_OWNERSHIP_OR_IS_INACTIVE);
        }
        return restaurant;
    }

    public ZoneId extractZoneId(String zoneIdStr) {
        if (zoneIdStr == null) {
            return null;
        }
        try {
            return ZoneId.of(zoneIdStr);
        } catch (DateTimeException ex) {
            throw new InvalidDataException(ZONE_ID_DO_NOT_EXIST);
        }
    }

}
