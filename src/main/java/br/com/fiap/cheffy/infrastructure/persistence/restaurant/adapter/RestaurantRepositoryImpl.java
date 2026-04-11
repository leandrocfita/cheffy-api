package br.com.fiap.cheffy.infrastructure.persistence.restaurant.adapter;

import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;
import br.com.fiap.cheffy.infrastructure.persistence.restaurant.entity.RestaurantJpaEntity;
import br.com.fiap.cheffy.infrastructure.persistence.restaurant.mapper.RestaurantPersistenceMapper;
import br.com.fiap.cheffy.infrastructure.persistence.restaurant.repository.RestaurantJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantPersistenceMapper restaurantMapper;


    @Override
    @Transactional
    public Restaurant save(Restaurant restaurant) {
        RestaurantJpaEntity restaurantJpaEntity = restaurantMapper.toJpa(restaurant);

        RestaurantJpaEntity saved = restaurantJpaRepository.save(restaurantJpaEntity);

        return restaurantMapper.toDomain(saved);
    }

    @Override
    public boolean existsByCnpj(String cnpj) {
        return restaurantJpaRepository.existsByCnpj(cnpj);
    }

    @Override
    public boolean existsByName(String restaurantName) {
        return restaurantJpaRepository.existsByName(restaurantName);
    }

    @Override
    public boolean existsActiveRestaurantByUserId(UUID userId) {
        return restaurantJpaRepository.existsByUserIdAndActiveTrue(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Restaurant> findById(UUID restaurantId) {
        return restaurantJpaRepository.findById(restaurantId)
                .map(restaurantMapper::toDomain);
    }
}
