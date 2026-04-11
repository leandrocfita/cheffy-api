package br.com.fiap.cheffy.infrastructure.persistence.fooditem.mapper;

import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.infrastructure.persistence.fooditem.entity.FoodItemJpaEntity;
import br.com.fiap.cheffy.infrastructure.persistence.restaurant.entity.RestaurantJpaEntity;
import br.com.fiap.cheffy.utils.FoodItemTestUtils;
import br.com.fiap.cheffy.utils.RestaurantTestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FoodItemPersistenceMapperTest {

    @InjectMocks
    private FoodItemPersistenceMapper mapper;

    @Test
    void toDomainMapsJpaEntityToDomain() {
        FoodItemJpaEntity entity = FoodItemTestUtils.createTestFoodItemJpaEntity();

        FoodItem result = mapper.toDomain(entity);

        assertThat(result.getId()).isEqualTo(entity.getId());
        assertThat(result.getName()).isEqualTo(entity.getName());
        assertThat(result.getPrice().value()).isEqualByComparingTo(entity.getPrice());
        assertThat(result.isAvailable()).isEqualTo(entity.getAvailable());
    }

    @Test
    void toJpaMapsDomainToJpaEntity() {
        FoodItem domain = FoodItemTestUtils.createTestFoodItemDomainEntity();
        RestaurantJpaEntity restaurantJpa = RestaurantTestUtils.createTestRestaurantJpaEntity();


        FoodItemJpaEntity result = mapper.toJpa(domain, restaurantJpa);

        assertThat(result.getId()).isEqualTo(domain.getId());
        assertThat(result.getName()).isEqualTo(domain.getName());
        assertThat(result.getPrice()).isEqualByComparingTo(domain.getPrice().value());
        assertThat(result.getDeliveryAvailable()).isEqualTo(domain.isDeliveryAvailable());
        assertThat(result.getAvailable()).isEqualTo(domain.isAvailable());
        assertThat(result.getActive()).isEqualTo(domain.isActive());
    }
}
