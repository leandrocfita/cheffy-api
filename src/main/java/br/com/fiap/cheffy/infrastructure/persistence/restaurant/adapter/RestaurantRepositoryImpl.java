package br.com.fiap.cheffy.infrastructure.persistence.restaurant.adapter;

import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;
import br.com.fiap.cheffy.infrastructure.persistence.restaurant.entity.RestaurantJpaEntity;
import br.com.fiap.cheffy.infrastructure.persistence.restaurant.mapper.RestaurantPersistenceMapper;
import br.com.fiap.cheffy.infrastructure.persistence.restaurant.repository.RestaurantJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantPersistenceMapper restaurantMapper;

    @Override
    @Transactional
    public Restaurant save(Restaurant restaurant) {
        log.debug("Saving restaurant {}", restaurant.getName());
        RestaurantJpaEntity restaurantJpaEntity = restaurantMapper.toJpa(restaurant);
        RestaurantJpaEntity saved = restaurantJpaRepository.save(restaurantJpaEntity);
        return restaurantMapper.toDomain(saved);
    }

    @Override
    public boolean existsByCnpj(String cnpj) {
        log.debug("Checking if restaurant exists by cnpj {}", cnpj);
        return restaurantJpaRepository.existsByCnpj(cnpj);
    }

    @Override
    public boolean existsByName(String restaurantName) {
        log.debug("Checking if restaurant exists by name {}", restaurantName);
        return restaurantJpaRepository.existsByName(restaurantName);
    }

    @Override
    public boolean existsActiveRestaurantByUserId(UUID userId) {
        log.debug("Checking if restaurant exists by userId {}", userId);
        return restaurantJpaRepository.existsByUserIdAndActiveTrue(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Restaurant> findById(UUID restaurantId) {
        log.debug("Finding restaurant by id {}", restaurantId);
        return restaurantJpaRepository.findById(restaurantId)
                .map(restaurantMapper::toDomain);
    }
}
