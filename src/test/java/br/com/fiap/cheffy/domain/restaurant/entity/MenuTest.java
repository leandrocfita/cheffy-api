package br.com.fiap.cheffy.domain.restaurant.entity;

import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MenuTest {

    @Test
    void availableItemsReturnsOnlyActiveAndAvailableItems() {
        FoodItem available = FoodItem.reconstitute(UUID.randomUUID(), "A", "D", BigDecimal.ONE, "k1", true, true, true);
        FoodItem unavailable = FoodItem.reconstitute(UUID.randomUUID(), "B", "D", BigDecimal.ONE, "k2", true, false, true);

        Menu menu = new Menu(new HashSet<>(Set.of(available, unavailable)));

        assertThat(menu.availableItems()).containsExactly(available);
    }

    @Test
    void removeItemThrowsWhenItemDoesNotExist() {
        Menu menu = new Menu(new HashSet<>());

        assertThrows(IllegalArgumentException.class, () -> menu.removeItem(UUID.randomUUID()));
    }

    @Test
    void deactivateDisablesAllItemsAndPreventsAddingNewOnes() {
        FoodItem item = FoodItem.reconstitute(UUID.randomUUID(), "A", "D", BigDecimal.ONE, "k1", true, true, true);
        Menu menu = new Menu(new HashSet<>(Set.of(item)));

        menu.deactivate();

        assertThat(item.isActive()).isFalse();
        assertThat(menu.availableItems()).isEmpty();
        assertThrows(IllegalStateException.class, () -> menu.addItem(FoodItem.reconstitute(UUID.randomUUID(), "B", "D", BigDecimal.ONE, "k2", true, true, true)));
    }

    @Test
    void hasActiveItemsReturnsTrueWhenActiveItemExists() {
        FoodItem active = FoodItem.reconstitute(UUID.randomUUID(), "A", "D", BigDecimal.ONE, "k1", true, true, true);
        Menu menu = new Menu(new HashSet<>(Set.of(active)));
        assertThat(menu.hasActiveItems()).isTrue();
    }

    @Test
    void hasActiveItemsReturnsFalseWhenEmpty() {
        Menu menu = new Menu(new HashSet<>());
        assertThat(menu.hasActiveItems()).isFalse();
    }

    @Test
    void hasActiveItemsReturnsFalseWhenAllInactive() {
        FoodItem inactive = FoodItem.reconstitute(UUID.randomUUID(), "A", "D", BigDecimal.ONE, "k1", false, true, false);
        Menu menu = new Menu(new HashSet<>(Set.of(inactive)));
        assertThat(menu.hasActiveItems()).isFalse();
    }
}
