package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.application.restaurant.dto.RestaurantCommandPort;
import br.com.fiap.cheffy.application.restaurant.dto.RestaurantQueryPort;
import br.com.fiap.cheffy.application.user.dto.AddressCommandPort;
import br.com.fiap.cheffy.domain.restaurant.port.input.*;
import br.com.fiap.cheffy.presentation.dto.RestaurantAddressCreateDTO;
import br.com.fiap.cheffy.presentation.dto.RestaurantCreateDTO;
import br.com.fiap.cheffy.presentation.mapper.RestaurantWebMapper;
import br.com.fiap.cheffy.utils.RestaurantTestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantControllerTest {

    @Mock
    private RegisterRestaurantInput restaurantInput;

    @Mock
    private DeactivateRestaurantInput deactivateRestaurantInput;

    @Mock
    private ReactivateRestaurantInput reactivateRestaurantInput;

    @Mock
    private UpdateRestaurantInput updateRestaurantInput;

    @Mock
    private FindRestaurantByIdInput findRestaurantByIdInput;

    @Mock
    private RestaurantWebMapper mapper;

    @InjectMocks
    private RestaurantController controller;

    @Test
    void registerRestaurantReturnsCreatedWithRestaurantId() {
        UUID userId = UUID.randomUUID();
        RestaurantCreateDTO dto = new RestaurantCreateDTO(
                "Restaurante",
                "Brasileira",
                "27865757000102",
                LocalTime.parse("09:00"),
                LocalTime.parse("18:00"),
                "America/Sao_Paulo",
                false,
                new RestaurantAddressCreateDTO("Rua B", 10, "São Paulo", "01001000", "Centro", "SP", "Casa")
        );
        AddressCommandPort addressCommandPort = new AddressCommandPort(
                dto.address().streetName(),
                dto.address().number(),
                dto.address().city(),
                dto.address().postalCode(),
                dto.address().neighborhood(),
                dto.address().stateProvince(),
                dto.address().addressLine(),
                null
        );

        RestaurantCommandPort command = new RestaurantCommandPort(
                dto.name(),
                dto.culinary(),
                dto.cnpj(),
                dto.openingTime(),
                dto.closingTime(),
                dto.zoneId(),
                dto.open24hours(),
                addressCommandPort
        );

        when(mapper.toCommand(dto)).thenReturn(command);
        when(restaurantInput.execute(command, userId)).thenReturn("restaurant-id");

        ResponseEntity<String> response = controller.registerRestaurant(dto, userId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo("restaurant-id");
        verify(mapper).toCommand(dto);
        verify(restaurantInput).execute(command, userId);
    }

    @Test
    void deactivateRestaurantReturnsNoContent() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        ResponseEntity<Void> response = controller.deactivateRestaurant(id, userId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(deactivateRestaurantInput).execute(id, userId);
    }

    @Test
    void reactivateRestaurantReturnsNoContent() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        ResponseEntity<Void> response = controller.reactivateRestaurant(id, userId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(reactivateRestaurantInput).execute(id, userId);
    }

    @Test
    void findRestaurantByIdReturnsOkWithRestaurantPayload() {
        UUID id = UUID.randomUUID();
        RestaurantQueryPort queryPort = RestaurantTestUtils.createTestRestaurantQueryPort();

        when(findRestaurantByIdInput.execute(id)).thenReturn(queryPort);

        ResponseEntity<RestaurantQueryPort> response = controller.findRestaurantById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(queryPort);
        verify(findRestaurantByIdInput).execute(id);
    }
}
