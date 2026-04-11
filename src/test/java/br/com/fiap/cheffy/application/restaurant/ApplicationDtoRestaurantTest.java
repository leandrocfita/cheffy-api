package br.com.fiap.cheffy.application.restaurant;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemQueryPort;
import br.com.fiap.cheffy.application.restaurant.dto.RestaurantCommandPort;
import br.com.fiap.cheffy.application.restaurant.dto.RestaurantQueryPort;
import br.com.fiap.cheffy.application.user.dto.AddressCommandPort;
import br.com.fiap.cheffy.application.user.dto.AddressQueryPort;
import br.com.fiap.cheffy.utils.FoodItemTestUtils;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationDtoRestaurantTest {

    @Test
    void restaurantCommandPortFields() {
        AddressCommandPort addr = new AddressCommandPort("St", 1, "City", "12345678", "Hood", "SP", null, true);
        RestaurantCommandPort dto = new RestaurantCommandPort("Name", "Brasileira", "27865757000102",
                LocalTime.of(9, 0), LocalTime.of(18, 0), "America/Sao_Paulo", false, addr);

        assertThat(dto.name()).isEqualTo("Name");
        assertThat(dto.cnpj()).isEqualTo("27865757000102");
        assertThat(dto.open24hours()).isFalse();
        assertThat(dto.address()).isEqualTo(addr);
    }

    @Test
    void restaurantQueryPortFields() {
        UUID id = UUID.randomUUID();
        UUID foodId = UUID.randomUUID();
        FoodItemQueryPort food = FoodItemTestUtils.createTestFoodItemQueryPort(foodId, id);
        AddressQueryPort addr = new AddressQueryPort(1l,"St", 1, "City", "12345678", "Hood", "SP", null, true);
        LocalTime opening = LocalTime.of(9, 0, 0, 0);
        LocalTime closing = LocalTime.of(18, 0, 0, 0);
        RestaurantQueryPort dto = new RestaurantQueryPort(id, "Name", "Brasileira", opening, closing,false, addr, id ,Set.of(food));

        assertThat(dto.id()).isEqualTo(id);
        assertThat(dto.name()).isEqualTo("Name");
        assertThat(dto.menu()).hasSize(1);
    }

    @Test
    void foodItemQueryPortFields() {
        UUID id = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        FoodItemQueryPort dto = FoodItemTestUtils.createTestFoodItemQueryPort(id, restaurantId);

        assertThat(dto.id()).isEqualTo(id);
        assertThat(dto.name()).isEqualTo("Test Food");
        assertThat(dto.price()).isEqualTo(new BigDecimal("19.99"));
        assertThat(dto.restaurantId()).isEqualTo(restaurantId);
        assertThat(dto.deliveryAvailable()).isTrue();
        assertThat(dto.available()).isTrue();
        assertThat(dto.active()).isTrue();
    }
}
