package br.com.fiap.cheffy.infrastructure.persistence.fooditem.repository;

import br.com.fiap.cheffy.infrastructure.persistence.fooditem.entity.FoodItemJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FoodItemJpaRepository extends JpaRepository<FoodItemJpaEntity, UUID> {

    @Query("""
        SELECT f FROM FoodItemJpaEntity f
        JOIN FETCH f.restaurant r
        WHERE r.id = :restaurantId
    """)
    List<FoodItemJpaEntity> findAllByRestaurantId(@Param("restaurantId") UUID restaurantId);

    @Query(value = """
        SELECT f FROM FoodItemJpaEntity f
        JOIN FETCH f.restaurant r
        WHERE r.id = :restaurantId
    """,
    countQuery = "SELECT COUNT(f) FROM FoodItemJpaEntity f WHERE f.restaurant.id = :restaurantId")
    Page<FoodItemJpaEntity> findAllByRestaurantId(@Param("restaurantId") UUID restaurantId, Pageable pageable);

    @Query(value = """
        SELECT f FROM FoodItemJpaEntity f
        JOIN FETCH f.restaurant r
        WHERE r.id = :restaurantId AND f.active = true
    """,
    countQuery = "SELECT COUNT(f) FROM FoodItemJpaEntity f WHERE f.restaurant.id = :restaurantId AND f.active = true")
    Page<FoodItemJpaEntity> findAllActiveByRestaurantId(@Param("restaurantId") UUID restaurantId, Pageable pageable);

    
    boolean existsByNameIgnoreCaseAndRestaurantId(String name, UUID restaurantId);

    @Query("""
        SELECT COUNT(f) > 0
        FROM FoodItemJpaEntity f
        WHERE f.id = :foodItemId
        AND f.restaurant.id = :restaurantId
    """)
    boolean existsInRestaurantById(@Param("restaurantId") UUID restaurantId, @Param("foodItemId") UUID foodItemId);


    @Query("""
        SELECT f FROM FoodItemJpaEntity f
        JOIN FETCH f.restaurant r
        WHERE f.id = :foodItemId
        AND r.id = :restaurantId
    """)
    Optional<FoodItemJpaEntity> findByIdAndRestaurantId(@Param("foodItemId") UUID foodItemId, @Param("restaurantId") UUID restaurantId);

}
