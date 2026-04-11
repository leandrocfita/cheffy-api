package br.com.fiap.cheffy.domain.fooditem.entity;

import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.utils.FoodItemTestUtils;
import br.com.fiap.cheffy.utils.RestaurantTestUtils;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FoodItemTest {

    @Test
    void createSetsRestaurantAndAvailabilityState() {
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        Restaurant restaurant = Restaurant.createWithWorkingHours(
                "Rest",
                "27865757000102",
                "Brasileira",
                zoneId,
                LocalTime.parse("09:00"),
                LocalTime.parse("18:00"),
                null
        );

        FoodItem item = FoodItem.create("Prato", "Desc", BigDecimal.TEN, "key", true, true, restaurant);

        assertThat(item.getRestaurant()).isEqualTo(restaurant);
        assertThat(item.isAvailable()).isTrue();
        assertThat(item.getPrice().value()).isEqualByComparingTo("10.00");
    }

    @Test
    void makeUnavailableAndDisableChangeAvailability() {
        FoodItem item = FoodItem.reconstitute(UUID.randomUUID(), "Prato", "Desc", BigDecimal.TEN, "key", true, true, true);

        item.makeUnavailable();
        assertThat(item.isAvailable()).isFalse();

        item.deactivate();
        assertThat(item.isActive()).isFalse();
        assertThat(item.isAvailable()).isFalse();
    }

    @Test
    void reactivateSetsActiveAndAvailable() {
        FoodItem item = FoodItem.reconstitute(UUID.randomUUID(), "Prato", "Desc", BigDecimal.TEN, "key", false, false, false);

        item.reactivate();

        assertThat(item.isActive()).isTrue();
        assertThat(item.isAvailable()).isTrue();
        assertThat(item.isDeliveryAvailable()).isTrue();
    }

    @Test
    void updateAvailabilityWithAvailableFalseSetsDeliveryFalse() {
        FoodItem item = FoodItem.reconstitute(UUID.randomUUID(), "Prato", "Desc", BigDecimal.TEN, "key", true, true, true);

        item.updateAvailability(false, null);

        assertThat(item.isAvailable()).isFalse();
        assertThat(item.isDeliveryAvailable()).isFalse();
    }

    @Test
    void updateAvailabilityWithNullAvailableKeepsCurrentAndUpdatesDelivery() {
        FoodItem item = FoodItem.reconstitute(UUID.randomUUID(), "Prato", "Desc", BigDecimal.TEN, "key", false, true, true);

        item.updateAvailability(null, true);

        assertThat(item.isDeliveryAvailable()).isTrue();
    }

    @Test
    void equalsAndHashCodeUseId() {
        UUID id = UUID.randomUUID();
        FoodItem first = FoodItem.reconstitute(id, "Prato", "Desc", BigDecimal.TEN, "key", true, true, true);
        FoodItem second = FoodItem.reconstitute(id, "Outro", "Outro", BigDecimal.ONE, "another", false, false, false);

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    void patchUpdatesItemAttributesSuccessfully() {
        FoodItem item = FoodItem.reconstitute(UUID.randomUUID(), "Prato Antigo", "Desc Antiga", BigDecimal.ONE, "oldKey", false, false, true);
        item.setRestaurant(RestaurantTestUtils.createTestRestaurantDomainEntity());

        FoodItem newItem = FoodItemTestUtils.createTestFoodItemDomainEntity();

        // Assuming patch takes these specific fields. Adjust the arguments if your patch method expects a DTO or different parameters.
        item.patch(newItem);

        assertThat(item.getName()).isEqualTo("Test Food");
        assertThat(item.getDescription()).isEqualTo("Delicious test food");
        assertThat(item.getPrice().value()).isEqualByComparingTo("19.99");
        assertThat(item.getPhotoKey()).isEqualTo("test-photo-key");
        assertThat(item.isDeliveryAvailable()).isTrue();
    }

    @Test
    void equalsReturnsFalseWhenNullId() {
        FoodItem withId = FoodItem.reconstitute(UUID.randomUUID(), "Prato", "Desc", BigDecimal.TEN, "key", true, true, true);
        FoodItem withoutId = FoodItem.create("Prato", "Desc", BigDecimal.TEN, "key", true, true, null);

        assertThat(withId).isNotEqualTo(withoutId);
    }

    @Test
    void equalsReturnsFalseForDifferentType() {
        FoodItem item = FoodItem.reconstitute(UUID.randomUUID(), "Prato", "Desc", BigDecimal.TEN, "key", true, true, true);

        assertThat(item).isNotEqualTo("string");
    }

    @Test
    void equalsReturnsTrueForSameInstance() {
        FoodItem item = FoodItem.reconstitute(UUID.randomUUID(), "Prato", "Desc", BigDecimal.TEN, "key", true, true, true);

        assertThat(item).isEqualTo(item);
    }

    @Test
    void hashCodeWithNullIdUsesIdentityHash() {
        FoodItem item = FoodItem.create("Prato", "Desc", BigDecimal.TEN, "key", true, true, null);

        assertThat(item.hashCode()).isEqualTo(System.identityHashCode(item));
    }

    @Test
    void gettersReturnCorrectValues() {
        UUID id = UUID.randomUUID();
        FoodItem item = FoodItem.reconstitute(id, "Prato", "Desc", BigDecimal.TEN, "key", true, true, true);

        assertThat(item.getId()).isEqualTo(id);
        assertThat(item.getName()).isEqualTo("Prato");
        assertThat(item.getDescription()).isEqualTo("Desc");
        assertThat(item.getPhotoKey()).isEqualTo("key");
        assertThat(item.isDeliveryAvailable()).isTrue();
    }
}
