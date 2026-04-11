package br.com.fiap.cheffy.infrastructure.persistence.restaurant.mapper;

import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.restaurant.entity.Menu;
import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.user.entity.Address;
import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.infrastructure.persistence.address.mapper.AddressPersistenceMapper;
import br.com.fiap.cheffy.infrastructure.persistence.fooditem.mapper.FoodItemPersistenceMapper;
import br.com.fiap.cheffy.infrastructure.persistence.restaurant.entity.RestaurantJpaEntity;
import br.com.fiap.cheffy.infrastructure.persistence.user.mapper.UserPersistenceMapper;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RestaurantPersistenceMapper {

    private AddressPersistenceMapper addressMapper;
    private UserPersistenceMapper userMapper;
    private FoodItemPersistenceMapper foodItemMapper;

    public RestaurantPersistenceMapper(
            AddressPersistenceMapper addressMapper,
            UserPersistenceMapper userMapper,
            FoodItemPersistenceMapper foodItemMapper
    ) {
        this.addressMapper = addressMapper;
        this.userMapper = userMapper;
        this.foodItemMapper = foodItemMapper;
    }

    public RestaurantJpaEntity toJpa(Restaurant restaurant) {
        RestaurantJpaEntity entity = new RestaurantJpaEntity();

        entity.setId(restaurant.getId());
        entity.setName(restaurant.getName());
        entity.setCnpj(restaurant.getCnpj());
        entity.setCulinary(restaurant.getCulinary());
        entity.setOpeningTime(restaurant.getOpeningTime());
        entity.setClosingTime(restaurant.getClosingTime());
        entity.setOpen24hours(restaurant.isOpen24hours());
        entity.setZoneId(restaurant.getZoneId().getId());
        entity.setActive(restaurant.isActive());

        if (restaurant.getAddress() != null) {
            entity.setAddress(addressMapper.toJpa(restaurant.getAddress()));
        }

        if (restaurant.getOwner() != null) {
            entity.setUser(userMapper.toJpa(restaurant.getOwner()));
        }

        return entity;

    }

    public Restaurant toDomain(RestaurantJpaEntity entity) {

        if (entity == null) return null;

        Address address = null;
        if (entity.getAddress() != null) {
            address = addressMapper.toDomain(entity.getAddress());
        }

        User owner = userMapper.toDomain(entity.getUser());

        Set<FoodItem> foodItems = entity.getFoodItems()
                .stream()
                .map(f -> foodItemMapper.toDomain(f))
                .collect(Collectors.toSet());


        Restaurant reconstitute = Restaurant.reconstitute(
                entity.getId(),
                entity.getName(),
                entity.getCnpj(),
                entity.getCulinary(),
                ZoneId.of(entity.getZoneId()),
                entity.getOpeningTime(),
                entity.getClosingTime(),
                entity.isOpen24hours(),
                entity.getActive(),
                address,
                owner,
                new Menu(foodItems)
        );


        return reconstitute;
    }

}
