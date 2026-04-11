//package br.com.fiap.cheffy.presentation.mapper;
//
//import br.com.fiap.cheffy.application.fooditem.dto.FoodItemCommandPort;
//import br.com.fiap.cheffy.application.fooditem.dto.FoodItemQueryPort;
//import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
//import br.com.fiap.cheffy.presentation.dto.FoodItemDTO;
//import br.com.fiap.cheffy.utils.FoodItemTestUtils;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ExtendWith(MockitoExtension.class)
//class FoodItemWebMapperTest {
//
//    @InjectMocks
//    private FoodItemWebMapper mapper;
//
//    @Test
//    @DisplayName("foodItemToFoodItemQueryPort maps FoodItem to FoodItemQueryPort with BigDecimal price")
//    void foodItemToFoodItemQueryPortMapsPriceAsBigDecimal() {
//        FoodItem foodItem = FoodItemTestUtils.createTestFoodItemDomainEntity();
//
//        FoodItemQueryPort result = mapper.foodItemToFoodItemQueryPort(foodItem);
//
//        assertThat(result.id()).isEqualTo(foodItem.getId());
//        assertThat(result.name()).isEqualTo(foodItem.getName());
//        assertThat(result.description()).isEqualTo(foodItem.getDescription());
//        assertThat(result.price()).isEqualByComparingTo(foodItem.getPrice().value());
//        assertThat(result.photoKey()).isEqualTo(foodItem.getPhotoKey());
//        assertThat(result.restaurantId()).isEqualTo(foodItem.getRestaurant().getId());
//        assertThat(result.deliveryAvailable()).isEqualTo(foodItem.isDeliveryAvailable());
//        assertThat(result.available()).isEqualTo(foodItem.isAvailable());
//        assertThat(result.active()).isEqualTo(foodItem.isActive());
//    }
//
//    @Test
//    @DisplayName("foodItemDtoToFoodItemCommandPort maps FoodItemDTO to FoodItemCommandPort")
//    void foodItemDtoToFoodItemCommandPortMapsCorrectly() {
//        UUID restaurantId = UUID.randomUUID();
//        FoodItemDTO dto = new FoodItemDTO("X-Burger", "desc", BigDecimal.TEN, "key", true, false, true);
//
//        FoodItemCommandPort result = mapper.foodItemDtoToFoodItemCommandPort(dto, restaurantId);
//
//        assertThat(result.name()).isEqualTo(dto.name());
//        assertThat(result.description()).isEqualTo(dto.description());
//        assertThat(result.price()).isEqualByComparingTo(dto.price());
//        assertThat(result.photoKey()).isEqualTo(dto.photoKey());
//        assertThat(result.restaurantId()).isEqualTo(restaurantId);
//        assertThat(result.deliveryAvailable()).isEqualTo(dto.deliveryAvailable());
//        assertThat(result.available()).isEqualTo(dto.available());
//    }
//}
