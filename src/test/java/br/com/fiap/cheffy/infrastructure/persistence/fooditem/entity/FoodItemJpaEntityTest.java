package br.com.fiap.cheffy.infrastructure.persistence.fooditem.entity;

import br.com.fiap.cheffy.infrastructure.persistence.restaurant.entity.RestaurantJpaEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FoodItemJpaEntityTest {

    @Test
    void setAndGetAllFields() {
        FoodItemJpaEntity entity = new FoodItemJpaEntity();
        UUID id = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();
        RestaurantJpaEntity restaurant = new RestaurantJpaEntity();

        entity.setId(id);
        entity.setName("Prato");
        entity.setDescription("Desc");
        entity.setPrice(BigDecimal.TEN);
        entity.setPhotoKey("key");
        entity.setRestaurant(restaurant);
        entity.setDeliveryAvailable(true);
        entity.setAvailable(false);
        entity.setActive(true);
        entity.setDateCreated(now);
        entity.setLastUpdated(now);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getName()).isEqualTo("Prato");
        assertThat(entity.getDescription()).isEqualTo("Desc");
        assertThat(entity.getPrice()).isEqualTo(BigDecimal.TEN);
        assertThat(entity.getPhotoKey()).isEqualTo("key");
        assertThat(entity.getRestaurant()).isEqualTo(restaurant);
        assertThat(entity.getDeliveryAvailable()).isTrue();
        assertThat(entity.getAvailable()).isFalse();
        assertThat(entity.getActive()).isTrue();
        assertThat(entity.getDateCreated()).isEqualTo(now);
        assertThat(entity.getLastUpdated()).isEqualTo(now);
    }
}
