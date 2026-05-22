package br.com.fiap.cheffy.application.fooditem;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemQueryPort;
import br.com.fiap.cheffy.application.fooditem.mapper.FoodItemQueryMapper;
import br.com.fiap.cheffy.application.fooditem.usecase.ListFoodItemsByRestaurantUseCase;
import br.com.fiap.cheffy.application.restaurant.dto.RestaurantQueryPort;
import br.com.fiap.cheffy.application.restaurant.mapper.ResturantQueryMapper;
import br.com.fiap.cheffy.application.restaurant.service.RestaurantServiceHelper;
import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.fooditem.port.output.FoodItemRepository;
import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantOperationNotAllowedException;
import br.com.fiap.cheffy.utils.FoodItemTestUtils;
import br.com.fiap.cheffy.utils.RestaurantTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListFoodItemsByRestaurantUseCaseTest {

    @Mock
    private FoodItemRepository foodItemRepository;

    @Mock
    private RestaurantServiceHelper restaurantServiceHelper;

    @InjectMocks
    private ListFoodItemsByRestaurantUseCase useCase;

    @Mock
    private FoodItemQueryMapper mapper;

    @Test
    @DisplayName("Should return paginated active food items for an active restaurant")
    void shouldReturnPaginatedFoodItemsForActiveRestaurant() {
        UUID restaurantId = UUID.randomUUID();
        PageRequest pageRequest = PageRequest.of(0, 10);
        Restaurant restaurant = RestaurantTestUtils.createTestRestaurantDomainEntity();
        FoodItem activeFoodItem = FoodItemTestUtils.createTestFoodItemDomainEntity();
        FoodItemQueryPort foodItemQueryPort = FoodItemTestUtils.createTestFoodItemQueryPort(UUID.randomUUID(), restaurantId);

        PageResult<FoodItem> page = PageResult.of(List.of(activeFoodItem), 0, 10, 1L);

        when(restaurantServiceHelper.getRestaurantOrFail(restaurantId)).thenReturn(restaurant);
        when(foodItemRepository.findAllActiveByRestaurantId(restaurantId, pageRequest)).thenReturn(page);
        when(mapper.toQueryPort(any(), any()))
                .thenAnswer(invocation -> {
                    FoodItem item = invocation.getArgument(0);
                    return foodItemQueryPort;
                });

        PageResult<FoodItemQueryPort> result = useCase.execute(restaurantId, pageRequest, false);

        assertThat(result.content()).hasSize(1);
        assertThat(result.content().get(0).name()).isEqualTo(activeFoodItem.getName());
        assertThat(result.content().get(0).price()).isEqualByComparingTo(activeFoodItem.getPrice().value());
        assertThat(result.totalElements()).isEqualTo(1L);
        verify(foodItemRepository).findAllActiveByRestaurantId(restaurantId, pageRequest);
    }

    @Test
    @DisplayName("Should filter out inactive food items")
    void shouldFilterInactiveFoodItems() {
        UUID restaurantId = UUID.randomUUID();
        PageRequest pageRequest = PageRequest.of(0, 10);
        Restaurant restaurant = RestaurantTestUtils.createTestRestaurantDomainEntity();
        FoodItemQueryPort foodItemQueryPort = FoodItemTestUtils.createTestFoodItemQueryPort(UUID.randomUUID(), restaurantId);

        FoodItem activeItem = FoodItemTestUtils.createTestFoodItemDomainEntity();
        FoodItem inactiveItem = FoodItem.reconstitute(
                UUID.randomUUID(), "Inactive", "desc",
                new java.math.BigDecimal("5.00"), "key", false, false, false
        );

        PageResult<FoodItem> page = PageResult.of(List.of(activeItem, inactiveItem), 0, 10, 2L);

        when(restaurantServiceHelper.getRestaurantOrFail(restaurantId)).thenReturn(restaurant);
        when(foodItemRepository.findAllByRestaurantId(restaurantId, pageRequest)).thenReturn(page);
        when(mapper.toQueryPort(any(), any()))
                .thenAnswer(invocation -> {
                    FoodItem item = invocation.getArgument(0);
                    return foodItemQueryPort;
                });

        PageResult<FoodItemQueryPort> result = useCase.execute(restaurantId, pageRequest, true);

        assertThat(result.content()).hasSize(2);
        assertThat(result.content().get(0).name()).isEqualTo(activeItem.getName());
    }

    @Test
    @DisplayName("Should throw exception when restaurant is inactive")
    void shouldThrowWhenRestaurantIsInactive() {
        UUID restaurantId = UUID.randomUUID();
        PageRequest pageRequest = PageRequest.of(0, 10);

        Restaurant inactiveRestaurant = RestaurantTestUtils.createTestRestaurantDomainEntity();
        inactiveRestaurant.deactivate();

        when(restaurantServiceHelper.getRestaurantOrFail(restaurantId)).thenReturn(inactiveRestaurant);

        assertThatThrownBy(() -> useCase.execute(restaurantId, pageRequest, false))
                .isInstanceOf(RestaurantOperationNotAllowedException.class);

        verifyNoInteractions(foodItemRepository);
    }

    @Test
    @DisplayName("Should propagate exception when restaurant is not found")
    void shouldPropagateExceptionWhenRestaurantNotFound() {
        UUID restaurantId = UUID.randomUUID();
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(restaurantServiceHelper.getRestaurantOrFail(restaurantId))
                .thenThrow(new br.com.fiap.cheffy.domain.restaurant.exception.RestaurantNotFoundException(
                        br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.RESTAURANT_NOT_FOUND_EXCEPTION, restaurantId));

        assertThatThrownBy(() -> useCase.execute(restaurantId, pageRequest, false))
                .isInstanceOf(br.com.fiap.cheffy.domain.restaurant.exception.RestaurantNotFoundException.class);

        verifyNoInteractions(foodItemRepository);
    }
}
