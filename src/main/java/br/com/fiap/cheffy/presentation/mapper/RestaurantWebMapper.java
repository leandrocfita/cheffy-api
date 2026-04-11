package br.com.fiap.cheffy.presentation.mapper;

import br.com.fiap.cheffy.application.restaurant.dto.RestaurantCommandPort;
import br.com.fiap.cheffy.application.restaurant.dto.UpdateRestaurantCommandPort;
import br.com.fiap.cheffy.application.user.dto.AddressCommandPort;
import br.com.fiap.cheffy.presentation.dto.RestaurantCreateDTO;
import br.com.fiap.cheffy.presentation.dto.RestaurantUpdateDTO;
import org.springframework.stereotype.Component;

@Component
public class RestaurantWebMapper {

    public RestaurantCommandPort toCommand(RestaurantCreateDTO dto) {
        return new RestaurantCommandPort(
                dto.name(),
                dto.culinary(),
                dto.cnpj(),
                dto.openingTime(),
                dto.closingTime(),
                dto.zoneId(),
                dto.open24hours(),
                new AddressCommandPort(
                        dto.address().streetName(),
                        dto.address().number(),
                        dto.address().city(),
                        dto.address().postalCode(),
                        dto.address().neighborhood(),
                        dto.address().stateProvince(),
                        dto.address().addressLine(),
                        null
                )

        );
    }

    public UpdateRestaurantCommandPort toUpdateCommand(RestaurantUpdateDTO dto) {
        return new UpdateRestaurantCommandPort(
                dto.name(),
                dto.culinary(),
                dto.openingTime(),
                dto.closingTime(),
                dto.zoneId(),
                dto.open24hours()
        );
    }
}
