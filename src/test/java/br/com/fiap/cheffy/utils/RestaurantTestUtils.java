//package br.com.fiap.cheffy.utils;
//
//import br.com.fiap.cheffy.application.fooditem.dto.FoodItemQueryPort;
//import br.com.fiap.cheffy.application.restaurant.dto.RestaurantQueryPort;
//import br.com.fiap.cheffy.domain.restaurant.entity.Menu;
//import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
//import br.com.fiap.cheffy.domain.user.entity.Address;
//import br.com.fiap.cheffy.infrastructure.persistence.address.entity.AddressJpaEntity;
//import br.com.fiap.cheffy.infrastructure.persistence.restaurant.entity.RestaurantJpaEntity;
//import br.com.fiap.cheffy.infrastructure.persistence.user.entity.UserJpaEntity;
//
//import java.time.LocalTime;
//import java.time.OffsetDateTime;
//import java.time.ZoneId;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.UUID;
//
//public class RestaurantTestUtils {
//
//    public static Restaurant createTestRestaurant24hDomainEntity(){
//        return Restaurant.create24h("teste", "cnpj-teste", "culinaria-teste", ZoneId.systemDefault(), UserTestUtils.createOwnerUserDomainEntity());
//    }
//
//    public static RestaurantQueryPort createTestRestaurantQueryPort(){
//        UUID restaurantId = UUID.randomUUID();
//        Set<FoodItemQueryPort> foodItems = new HashSet<>();
//        foodItems.add(FoodItemTestUtils.createTestFoodItemQueryPort(UUID.randomUUID(),restaurantId));
//        return new RestaurantQueryPort(
//                restaurantId,
//                "Test Restaurant",
//                "Brazilian",
//                LocalTime.of(10, 0),
//                LocalTime.of(22, 0),
//                false,
//                AddressTestUtils.createAddressQueryPort(),
//                UUID.randomUUID(),
//                foodItems
//
//
//
//        );
//    }
//
//    public static Restaurant createTestRestaurantDomainEntity() {
//        UUID id = UUID.randomUUID();
//        return Restaurant.reconstitute(
//                id,
//                "Test Restaurant",
//                "12345678000199",
//                "Italian",
//                ZoneId.systemDefault(),
//                LocalTime.of(10, 0),
//                LocalTime.of(22, 0),
//                false,
//                true,
//                new Address(1L, "Main St", 123, "Test City", "12345678", "Test Hood", "TS", null, true),
//                UserTestUtils.createOwnerUserDomainEntity(),
//                new Menu(new HashSet<>())
//        );
//    }
//
//    public static RestaurantJpaEntity createTestRestaurantJpaEntity() {
//        RestaurantJpaEntity entity = new RestaurantJpaEntity();
//        entity.setId(UUID.randomUUID());
//        entity.setName("Test Restaurant");
//        entity.setCnpj("12345678000199");
//        entity.setCulinary("Italian");
//        entity.setOpeningTime(LocalTime.of(10, 0));
//        entity.setClosingTime(LocalTime.of(22, 0));
//        entity.setOpen24hours(false);
//        entity.setZoneId(ZoneId.systemDefault().getId());
//
//        AddressJpaEntity address = new AddressJpaEntity();
//        address.setStreetName("Main St");
//        address.setCity("Test City");
//        address.setNumber(123);
//        address.setPostalCode("12345678");
//        address.setNeighborhood("Test Hood");
//        address.setStateProvince("TS");
//        address.setMain(true);
//        entity.setAddress(address);
//
//        UserJpaEntity user = new UserJpaEntity();
//        user.setId(UUID.randomUUID());
//        user.setName("Test Owner");
//        entity.setUser(user);
//
//        entity.setFoodItems(new HashSet<>());
//        entity.setActive(true);
//        entity.setDateCreated(OffsetDateTime.now());
//        entity.setLastUpdated(OffsetDateTime.now());
//
//        return entity;
//    }
//}
