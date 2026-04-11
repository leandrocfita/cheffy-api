package br.com.fiap.cheffy.infrastructure.persistence.fooditem.mapper;

import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.infrastructure.persistence.fooditem.entity.FoodItemJpaEntity;
import br.com.fiap.cheffy.infrastructure.persistence.restaurant.entity.RestaurantJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class FoodItemPersistenceMapper {

    public FoodItemPersistenceMapper(
    ) {
    }

    public FoodItem toDomain(FoodItemJpaEntity entity) {
        FoodItem domainEntity = FoodItem.reconstitute(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getPhotoKey(),
                entity.getDeliveryAvailable(),
                entity.getAvailable(),
                entity.getActive()
        );

        return  domainEntity;
    }

    public FoodItemJpaEntity toJpa(FoodItem domain, RestaurantJpaEntity restaurantJpaEntity) {

        FoodItemJpaEntity entity = new FoodItemJpaEntity();

        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setPrice(domain.getPrice().value());
        entity.setPhotoKey(domain.getPhotoKey());
        entity.setDeliveryAvailable(domain.isDeliveryAvailable());
        entity.setAvailable(domain.isAvailable());
        entity.setActive(domain.isActive());
        entity.setRestaurant(restaurantJpaEntity);

        return entity;
    }
}
