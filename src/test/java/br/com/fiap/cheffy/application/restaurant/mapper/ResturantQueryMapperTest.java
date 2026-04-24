//package br.com.fiap.cheffy.application.restaurant.mapper;
//
//import br.com.fiap.cheffy.application.address.mapper.AddressQueryMapper;
//import br.com.fiap.cheffy.application.fooditem.mapper.FoodItemQueryMapper;
//import br.com.fiap.cheffy.application.restaurant.dto.RestaurantQueryPort;
//import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
//import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
//import br.com.fiap.cheffy.utils.FoodItemTestUtils;
//import br.com.fiap.cheffy.utils.RestaurantTestUtils;
//import org.junit.jupiter.api.Test;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class ResturantQueryMapperTest {
//
//    private final ResturantQueryMapper mapper = new ResturantQueryMapper(
//            new AddressQueryMapper(), new FoodItemQueryMapper());
//
//    @Test
//    void shouldMapRestaurantToQueryPortWithOwnerAndMenu() {
//        Restaurant restaurant = RestaurantTestUtils.createTestRestaurantDomainEntity();
//        FoodItem foodItem = FoodItemTestUtils.createTestFoodItemDomainEntity();
//        restaurant.addFoodItem(foodItem);
//
//        RestaurantQueryPort result = mapper.toQueryPort(restaurant, restaurant.getOwner().getId());
//
//        assertThat(result.id()).isEqualTo(restaurant.getId());
//        assertThat(result.name()).isEqualTo(restaurant.getName());
//        assertThat(result.culinary()).isEqualTo(restaurant.getCulinary());
//        assertThat(result.openingTime()).isEqualTo(restaurant.getOpeningTime());
//        assertThat(result.closingTime()).isEqualTo(restaurant.getClosingTime());
//        assertThat(result.open24hours()).isEqualTo(restaurant.isOpen24hours());
//        assertThat(result.ownerId()).isEqualTo(restaurant.getOwner().getId());
//        assertThat(result.address()).isNotNull();
//        assertThat(result.address().streetName()).isEqualTo(restaurant.getAddress().getStreetName());
//        assertThat(result.menu()).singleElement().satisfies(item -> {
//            assertThat(item.id()).isEqualTo(foodItem.getId());
//            assertThat(item.restaurantId()).isEqualTo(restaurant.getId());
//        });
//    }
//}
