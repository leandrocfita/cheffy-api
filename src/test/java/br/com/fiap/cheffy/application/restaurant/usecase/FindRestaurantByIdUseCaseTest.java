//package br.com.fiap.cheffy.application.restaurant.usecase;
//
//import br.com.fiap.cheffy.application.restaurant.dto.RestaurantQueryPort;
//import br.com.fiap.cheffy.application.restaurant.mapper.ResturantQueryMapper;
//import br.com.fiap.cheffy.application.restaurant.service.RestaurantServiceHelper;
//import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
//import br.com.fiap.cheffy.utils.RestaurantTestUtils;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Set;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class FindRestaurantByIdUseCaseTest {
//
//    @Mock
//    private RestaurantServiceHelper restaurantServiceHelper;
//
//    @Mock
//    private ResturantQueryMapper mapper;
//
//    @InjectMocks
//    private FindRestaurantByIdUseCase useCase;
//
//    @Test
//    void shouldReturnRestaurantQueryPortWhenRestaurantExists() {
//        UUID id = UUID.randomUUID();
//        Restaurant restaurant = RestaurantTestUtils.createTestRestaurantDomainEntity();
//        RestaurantQueryPort expected = new RestaurantQueryPort(
//                restaurant.getId(),
//                restaurant.getName(),
//                restaurant.getCulinary(),
//                restaurant.getOpeningTime(),
//                restaurant.getClosingTime(),
//                false,
//                null,
//                restaurant.getOwner().getId(),
//                Set.of()
//        );
//
//        when(restaurantServiceHelper.getRestaurantOrFail(id)).thenReturn(restaurant);
//        when(mapper.toQueryPort(restaurant, restaurant.getOwner().getId())).thenReturn(expected);
//
//        RestaurantQueryPort result = useCase.execute(id);
//
//        assertThat(result).isEqualTo(expected);
//        verify(restaurantServiceHelper).getRestaurantOrFail(id);
//        verify(mapper).toQueryPort(restaurant, restaurant.getOwner().getId());
//    }
//}
