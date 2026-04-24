//package br.com.fiap.cheffy.infrastructure.persistence.restaurant.mapper;
//
//import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
//import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
//import br.com.fiap.cheffy.domain.user.entity.Address;
//import br.com.fiap.cheffy.domain.user.entity.User;
//import br.com.fiap.cheffy.infrastructure.persistence.address.entity.AddressJpaEntity;
//import br.com.fiap.cheffy.infrastructure.persistence.address.mapper.AddressPersistenceMapper;
//import br.com.fiap.cheffy.infrastructure.persistence.fooditem.entity.FoodItemJpaEntity;
//import br.com.fiap.cheffy.infrastructure.persistence.fooditem.mapper.FoodItemPersistenceMapper;
//import br.com.fiap.cheffy.infrastructure.persistence.restaurant.entity.RestaurantJpaEntity;
//import br.com.fiap.cheffy.infrastructure.persistence.user.entity.UserJpaEntity;
//import br.com.fiap.cheffy.infrastructure.persistence.user.mapper.UserPersistenceMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.time.LocalTime;
//import java.time.ZoneId;
//import java.util.Set;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class RestaurantPersistenceMapperTest {
//
//    @Mock
//    private AddressPersistenceMapper addressMapper;
//    @Mock
//    private UserPersistenceMapper userMapper;
//    @Mock
//    private FoodItemPersistenceMapper foodItemMapper;
//
//    private RestaurantPersistenceMapper mapper;
//
//    @BeforeEach
//    void setUp() {
//        mapper = new RestaurantPersistenceMapper(addressMapper, userMapper, foodItemMapper);
//    }
//
//    @Test
//    void toJpaMapsRestaurantIncludingAddressAndUser() {
//        User owner = new User(UUID.randomUUID(), "Owner", "mail@test.com", "owner", "Pass@12345678", true);
//        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
//
//        Restaurant restaurant = Restaurant.createWithWorkingHours(
//                "Restaurante",
//                "27865757000102",
//                "Brasileira",
//                zoneId,
//                LocalTime.parse("09:00"),
//                LocalTime.parse("18:00"),
//                owner
//        );
//        restaurant.addAddress(new Address(1L, "Rua A", 10, "São Paulo", "01001000", "Centro", "SP", "Casa", true));
//
//        when(addressMapper.toJpa(any())).thenReturn(new AddressJpaEntity());
//        when(userMapper.toJpa(owner)).thenReturn(new UserJpaEntity());
//
//        RestaurantJpaEntity result = mapper.toJpa(restaurant);
//
//        assertThat(result.getName()).isEqualTo("Restaurante");
//        assertThat(result.getAddress()).isNotNull();
//        assertThat(result.getUser()).isNotNull();
//    }
//
//    @Test
//    void toDomainMapsJpaEntityWithFoodItems() {
//        UUID id = UUID.randomUUID();
//        RestaurantJpaEntity entity = new RestaurantJpaEntity();
//        entity.setId(id);
//        entity.setName("Restaurante");
//        entity.setCnpj("27865757000102");
//        entity.setCulinary("Brasileira");
//        entity.setZoneId("America/Sao_Paulo");
//        entity.setOpeningTime(LocalTime.parse("09:00"));
//        entity.setClosingTime(LocalTime.parse("18:00"));
//        entity.setActive(true);
//        entity.setAddress(new AddressJpaEntity());
//        entity.setUser(new UserJpaEntity());
//        FoodItemJpaEntity foodItemJpa = new FoodItemJpaEntity();
//        foodItemJpa.setId(UUID.randomUUID());
//        entity.setFoodItems(Set.of(foodItemJpa));
//
//        Address address = new Address(1L, "Rua A", 10, "São Paulo", "01001000", "Centro", "SP", "Casa", true);
//        User owner = new User(UUID.randomUUID(), "Owner", "mail@test.com", "owner", "Pass@12345678", true);
//        FoodItem foodItem = FoodItem.reconstitute(
//                foodItemJpa.getId(),
//                "Prato",
//                "Descrição",
//                BigDecimal.TEN,
//                "key",
//                true,
//                true,
//                true
//        );
//
//        when(addressMapper.toDomain(any())).thenReturn(address);
//        when(userMapper.toDomain(any())).thenReturn(owner);
//        when(foodItemMapper.toDomain(any())).thenReturn(foodItem);
//
//        Restaurant result = mapper.toDomain(entity);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getId()).isEqualTo(id);
//        assertThat(result.getAddress()).isEqualTo(address);
//        assertThat(result.getOwner()).isEqualTo(owner);
//        assertThat(result.getMenu().getItems()).hasSize(1);
//    }
//
//    @Test
//    void toDomainReturnsNullWhenEntityIsNull() {
//        assertThat(mapper.toDomain(null)).isNull();
//    }
//}
