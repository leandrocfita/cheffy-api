package br.com.fiap.cheffy.presentation.mapper;

import br.com.fiap.cheffy.application.restaurant.dto.RestaurantCommandPort;
import br.com.fiap.cheffy.presentation.dto.RestaurantAddressCreateDTO;
import br.com.fiap.cheffy.presentation.dto.RestaurantCreateDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.OffsetTime;

import static org.assertj.core.api.Assertions.assertThat;

class RestaurantWebMapperTest {

    private final RestaurantWebMapper mapper = new RestaurantWebMapper();

    @Test
    void toCommandMapsRestaurantAndAddressFields() {
        RestaurantCreateDTO dto = new RestaurantCreateDTO(
                "Restaurante Legal",
                "Italiana",
                "27865757000102",
                LocalTime.parse("09:00"),
                LocalTime.parse("18:00"),
                "America/Sao_Paulo",
                false,
                new RestaurantAddressCreateDTO(
                        "Rua A",
                        100,
                        "São Paulo",
                        "01001000",
                        "Centro",
                        "SP",
                        "Sala 10"
                )
        );

        RestaurantCommandPort command = mapper.toCommand(dto);

        assertThat(command.name()).isEqualTo(dto.name());
        assertThat(command.culinary()).isEqualTo(dto.culinary());
        assertThat(command.cnpj()).isEqualTo(dto.cnpj());
        assertThat(command.address().streetName()).isEqualTo(dto.address().streetName());
        assertThat(command.address().main()).isNull();
    }
}

class ProfileWebMapperTest {
    @Test
    void toProfileInputCommandPortMapsType() {
        br.com.fiap.cheffy.presentation.dto.ProfileInputDto dto =
                new br.com.fiap.cheffy.presentation.dto.ProfileInputDto("CLIENT");
        br.com.fiap.cheffy.application.profile.dto.ProfileInputPort result =
                ProfileWebMapper.toProfileInputCommandPort(dto);
        assertThat(result.name()).isEqualTo("CLIENT");
    }
}
