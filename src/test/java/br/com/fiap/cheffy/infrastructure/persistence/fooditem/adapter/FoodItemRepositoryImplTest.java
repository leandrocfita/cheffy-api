//package br.com.fiap.cheffy.infrastructure.persistence.fooditem.adapter;
//
//import br.com.fiap.cheffy.domain.common.PageRequest;
//import br.com.fiap.cheffy.domain.common.PageResult;
//import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
//import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
//import br.com.fiap.cheffy.infrastructure.persistence.fooditem.entity.FoodItemJpaEntity;
//import br.com.fiap.cheffy.infrastructure.persistence.fooditem.mapper.FoodItemPersistenceMapper;
//import br.com.fiap.cheffy.infrastructure.persistence.fooditem.repository.FoodItemJpaRepository;
//import br.com.fiap.cheffy.infrastructure.persistence.restaurant.entity.RestaurantJpaEntity;
//import br.com.fiap.cheffy.infrastructure.persistence.restaurant.mapper.RestaurantPersistenceMapper;
//import br.com.fiap.cheffy.utils.FoodItemTestUtils;
//import org.junit.jupiter.api.DisplayName;
//import br.com.fiap.cheffy.utils.RestaurantTestUtils;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//
//import java.util.Optional;
//import java.util.List;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class FoodItemRepositoryImplTest {
//
//    @Mock
//    private FoodItemPersistenceMapper foodItemPersistenceMapper;
//
//    @Mock
//    private FoodItemJpaRepository foodItemJpaRepository;
//
//    @Mock
//    private RestaurantPersistenceMapper restaurantPersistenceMapper;
//
//    @InjectMocks
//    private FoodItemRepositoryImpl repository;
//
//    @Test
//    void findByIdAndRestaurantIdReturnsMappedDomainWhenFound() {
//        UUID foodItemId = UUID.randomUUID();
//        UUID restaurantId = UUID.randomUUID();
//        FoodItemJpaEntity jpaEntity = FoodItemTestUtils.createTestFoodItemJpaEntity();
//        FoodItem domainFoodItem = FoodItemTestUtils.createTestFoodItemDomainEntity();
//        Restaurant domainRestaurant = RestaurantTestUtils.createTestRestaurantDomainEntity();
//        RestaurantJpaEntity restaurantJpaEntity = jpaEntity.getRestaurant();
//        when(foodItemJpaRepository.findByIdAndRestaurantId(foodItemId, restaurantId)).thenReturn(Optional.of(jpaEntity));
//        when(foodItemPersistenceMapper.toDomain(jpaEntity)).thenReturn(domainFoodItem);
//        when(restaurantPersistenceMapper.toDomain(restaurantJpaEntity)).thenReturn(domainRestaurant);
//        Optional<FoodItem> result = repository.findByIdAndRestaurantId(foodItemId, restaurantId);
//        assertThat(result).isPresent();
//        assertThat(result.get().getRestaurant()).isEqualTo(domainRestaurant);
//        verify(foodItemJpaRepository).findByIdAndRestaurantId(foodItemId, restaurantId);
//        verify(foodItemPersistenceMapper).toDomain(jpaEntity);
//        verify(restaurantPersistenceMapper).toDomain(restaurantJpaEntity);
//    }
//
//    @Test
//    void findByIdAndRestaurantIdReturnsEmptyWhenNotFound() {
//        UUID foodItemId = UUID.randomUUID();
//        UUID restaurantId = UUID.randomUUID();
//        when(foodItemJpaRepository.findByIdAndRestaurantId(foodItemId, restaurantId)).thenReturn(Optional.empty());
//        Optional<FoodItem> result = repository.findByIdAndRestaurantId(foodItemId, restaurantId);
//        assertThat(result).isEmpty();
//        verify(foodItemJpaRepository).findByIdAndRestaurantId(foodItemId, restaurantId);
//        verify(foodItemPersistenceMapper, never()).toDomain(any(FoodItemJpaEntity.class));
//    }
//
//    @Test
//    void findByIdReturnsMappedDomainWhenFound() {
//        UUID foodItemId = UUID.randomUUID();
//        FoodItemJpaEntity jpaEntity = FoodItemTestUtils.createTestFoodItemJpaEntity();
//        FoodItem domainFoodItem = FoodItemTestUtils.createTestFoodItemDomainEntity();
//        Restaurant domainRestaurant = RestaurantTestUtils.createTestRestaurantDomainEntity();
//        RestaurantJpaEntity restaurantJpaEntity = jpaEntity.getRestaurant();
//        when(foodItemJpaRepository.findById(foodItemId)).thenReturn(Optional.of(jpaEntity));
//        when(foodItemPersistenceMapper.toDomain(jpaEntity)).thenReturn(domainFoodItem);
//        when(restaurantPersistenceMapper.toDomain(restaurantJpaEntity)).thenReturn(domainRestaurant);
//        Optional<FoodItem> result = repository.findById(foodItemId);
//        assertThat(result).isPresent();
//        assertThat(result.get().getRestaurant()).isEqualTo(domainRestaurant);
//        verify(foodItemJpaRepository).findById(foodItemId);
//        verify(foodItemPersistenceMapper).toDomain(jpaEntity);
//    }
//
//    @Test
//    void findByIdReturnsEmptyWhenNotFound() {
//        UUID foodItemId = UUID.randomUUID();
//        when(foodItemJpaRepository.findById(foodItemId)).thenReturn(Optional.empty());
//        Optional<FoodItem> result = repository.findById(foodItemId);
//        assertThat(result).isEmpty();
//        verify(foodItemJpaRepository).findById(foodItemId);
//    }
//
//    @Test
//    @DisplayName("findAllByRestaurantId with pagination returns mapped domain page")
//    void findAllByRestaurantIdPaginatedReturnsMappedPage() {
//        UUID restaurantId = UUID.randomUUID();
//        PageRequest pageRequest = PageRequest.of(0, 10);
//        FoodItemJpaEntity jpaEntity = FoodItemTestUtils.createTestFoodItemJpaEntity();
//        FoodItem domainItem = FoodItemTestUtils.createTestFoodItemDomainEntity();
//
//        Page<FoodItemJpaEntity> springPage = new PageImpl<>(List.of(jpaEntity));
//        when(foodItemJpaRepository.findAllByRestaurantId(eq(restaurantId), any(Pageable.class))).thenReturn(springPage);
//        when(foodItemPersistenceMapper.toDomain(jpaEntity)).thenReturn(domainItem);
//
//        PageResult<FoodItem> result = repository.findAllByRestaurantId(restaurantId, pageRequest);
//
//        assertThat(result.content()).hasSize(1);
//        assertThat(result.content().get(0)).isEqualTo(domainItem);
//        assertThat(result.totalElements()).isEqualTo(1L);
//    }
//
//    @Test
//    @DisplayName("findAllByRestaurantId with pagination returns empty page when no items")
//    void findAllByRestaurantIdPaginatedReturnsEmptyPage() {
//        UUID restaurantId = UUID.randomUUID();
//        PageRequest pageRequest = PageRequest.of(0, 10);
//
//        Page<FoodItemJpaEntity> emptyPage = new PageImpl<>(List.of());
//        when(foodItemJpaRepository.findAllByRestaurantId(eq(restaurantId), any(Pageable.class))).thenReturn(emptyPage);
//
//        PageResult<FoodItem> result = repository.findAllByRestaurantId(restaurantId, pageRequest);
//
//        assertThat(result.content()).isEmpty();
//        assertThat(result.totalElements()).isZero();
//    }
//}
