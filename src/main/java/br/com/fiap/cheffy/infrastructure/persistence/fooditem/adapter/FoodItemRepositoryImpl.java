package br.com.fiap.cheffy.infrastructure.persistence.fooditem.adapter;

import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.fooditem.port.output.FoodItemRepository;
import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.infrastructure.persistence.fooditem.entity.FoodItemJpaEntity;
import br.com.fiap.cheffy.infrastructure.persistence.fooditem.mapper.FoodItemPersistenceMapper;
import br.com.fiap.cheffy.infrastructure.persistence.fooditem.repository.FoodItemJpaRepository;
import br.com.fiap.cheffy.infrastructure.persistence.pagination.PageMapper;
import br.com.fiap.cheffy.infrastructure.persistence.restaurant.entity.RestaurantJpaEntity;
import br.com.fiap.cheffy.infrastructure.persistence.restaurant.mapper.RestaurantPersistenceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FoodItemRepositoryImpl implements FoodItemRepository {

    private final FoodItemPersistenceMapper foodItemPersistenceMapper;
    private final FoodItemJpaRepository foodItemJpaRepository;
    private final RestaurantPersistenceMapper restaurantPersistenceMapper;


    @Override
    public FoodItem save(FoodItem foodItem) {

        RestaurantJpaEntity restaurantJpaEntity = restaurantPersistenceMapper.toJpa(foodItem.getRestaurant());

        FoodItemJpaEntity transformedObject = foodItemPersistenceMapper.toJpa(foodItem,  restaurantJpaEntity);

         var savedEntity = foodItemJpaRepository.save(transformedObject);

        FoodItem saved = foodItemPersistenceMapper.toDomain(savedEntity);
        saved.setRestaurant(foodItem.getRestaurant());

        return saved;
    }

    @Override
    public Optional<FoodItem> findById(UUID foodItemId) {
        return foodItemJpaRepository.findById(foodItemId)
                .map(this::mapToDomainWithRestaurant);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FoodItem> findByIdAndRestaurantId(UUID foodItemId, UUID restaurantId) {
        return foodItemJpaRepository.findByIdAndRestaurantId(foodItemId, restaurantId)
                .map(entity -> {
                    FoodItem foodItem = foodItemPersistenceMapper.toDomain(entity);
                    Restaurant restaurant = restaurantPersistenceMapper.toDomain(entity.getRestaurant());
                    foodItem.setRestaurant(restaurant);
                    return foodItem;
                });
    }

    private FoodItem mapToDomainWithRestaurant(FoodItemJpaEntity entity) {
        FoodItem foodItem = foodItemPersistenceMapper.toDomain(entity);
        if (entity.getRestaurant() != null) {
            foodItem.setRestaurant(restaurantPersistenceMapper.toDomain(entity.getRestaurant()));
        }
        return foodItem;
    }

    @Override
    public List<FoodItem> findAllByRestaurantId(UUID restaurantId) {
        return List.of();
    }

    @Override
    public PageResult<FoodItem> findAllByRestaurantId(UUID restaurantId, PageRequest pageRequest) {
        Pageable springPageable = PageMapper.toSpringPageable(pageRequest);
        Page<FoodItemJpaEntity> springPage = foodItemJpaRepository.findAllByRestaurantId(restaurantId, springPageable);
        Page<FoodItem> domainPage = springPage.map(foodItemPersistenceMapper::toDomain);
        return PageMapper.toDomainPageResult(domainPage);
    }

    @Override
    public PageResult<FoodItem> findAllActiveByRestaurantId(UUID restaurantId, PageRequest pageRequest) {
        Pageable springPageable = PageMapper.toSpringPageable(pageRequest);
        Page<FoodItemJpaEntity> springPage = foodItemJpaRepository.findAllActiveByRestaurantId(restaurantId, springPageable);
        Page<FoodItem> domainPage = springPage.map(foodItemPersistenceMapper::toDomain);
        return PageMapper.toDomainPageResult(domainPage);
    }

    @Override
    public boolean existsInRestaurantById(UUID restaurantId, UUID foodItemId) {
        return foodItemJpaRepository.existsInRestaurantById(restaurantId, foodItemId);
    }

    @Override
    public boolean existsByNameIgnoreCaseAndRestaurantId(String foodName, UUID restaurantId) {
        return foodItemJpaRepository.existsByNameIgnoreCaseAndRestaurantId(foodName, restaurantId);
    }
}
