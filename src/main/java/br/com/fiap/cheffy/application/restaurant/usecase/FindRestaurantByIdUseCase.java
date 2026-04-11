package br.com.fiap.cheffy.application.restaurant.usecase;


import br.com.fiap.cheffy.application.restaurant.dto.RestaurantQueryPort;
import br.com.fiap.cheffy.application.restaurant.mapper.ResturantQueryMapper;
import br.com.fiap.cheffy.application.restaurant.service.RestaurantServiceHelper;
import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.restaurant.port.input.FindRestaurantByIdInput;

import java.util.UUID;

public class FindRestaurantByIdUseCase implements FindRestaurantByIdInput {

    private RestaurantServiceHelper restaurantServiceHelper;
    private ResturantQueryMapper mapper;

    public FindRestaurantByIdUseCase(
            RestaurantServiceHelper restaurantServiceHelper,
            ResturantQueryMapper mapper
            ) {
        this.restaurantServiceHelper = restaurantServiceHelper;
        this.mapper = mapper;
    }

    @Override
    public RestaurantQueryPort execute(UUID id) {

        Restaurant restaurant = restaurantServiceHelper.getRestaurantOrFail(id);

        return mapper.toQueryPort(restaurant, restaurant.getOwner().getId());
    }
}
