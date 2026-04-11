package br.com.fiap.cheffy.domain.restaurant.port.output;

import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;

import java.util.Optional;
import java.util.UUID;

public interface RestaurantRepository {

    Restaurant save(Restaurant restaurant);

    boolean existsByCnpj(String cnpj);

    boolean existsByName(String restaurantName);

    boolean existsActiveRestaurantByUserId(UUID userId);

    Optional<Restaurant> findById(UUID restaurantId);
}
