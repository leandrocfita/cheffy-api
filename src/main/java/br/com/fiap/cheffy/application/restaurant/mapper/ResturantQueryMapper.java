package br.com.fiap.cheffy.application.restaurant.mapper;

import br.com.fiap.cheffy.application.address.mapper.AddressQueryMapper;
import br.com.fiap.cheffy.application.fooditem.mapper.FoodItemQueryMapper;
import br.com.fiap.cheffy.application.restaurant.dto.RestaurantQueryPort;
import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;

import java.util.UUID;
import java.util.stream.Collectors;

public class ResturantQueryMapper {

    private AddressQueryMapper addressQueryMapper;
    private FoodItemQueryMapper foodItemQueryMapper;

    public ResturantQueryMapper(
            AddressQueryMapper addressQueryMapper,
            FoodItemQueryMapper foodItemQueryMapper
    ) {
        this.addressQueryMapper = addressQueryMapper;
        this.foodItemQueryMapper = foodItemQueryMapper;
    }

    public RestaurantQueryPort toQueryPort(Restaurant restaurant, UUID ownerId) {

        return new RestaurantQueryPort(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCulinary(),
                restaurant.getOpeningTime(),
                restaurant.getClosingTime(),
                restaurant.isOpen24hours(),
                addressQueryMapper.toQueryPort(restaurant.getAddress()),
                ownerId,
                restaurant.getMenu().getItems()
                        .stream()
                        .map(item -> foodItemQueryMapper.toQueryPort(item, restaurant.getId()))
                        .collect(Collectors.toSet())

        );
    }
}
