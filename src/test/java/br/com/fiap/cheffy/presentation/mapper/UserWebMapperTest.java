package br.com.fiap.cheffy.presentation.mapper;

import br.com.fiap.cheffy.application.user.dto.AddressCommandPort;
import br.com.fiap.cheffy.application.user.dto.UserCommandPort;
import br.com.fiap.cheffy.presentation.dto.AddressCreateDTO;
import br.com.fiap.cheffy.presentation.dto.AddressPatchDTO;
import br.com.fiap.cheffy.presentation.dto.UserCreateDTO;
import br.com.fiap.cheffy.presentation.dto.UserUpdateDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserWebMapperTest {

    @Test
    void toCommandFromUserCreateDTO() {
        UserWebMapper mapper = new UserWebMapper();
        AddressCreateDTO addressDTO = new AddressCreateDTO("St", 1, "City", "12345678", "Hood", "ST", null, true);
        UserCreateDTO dto = new UserCreateDTO("Name", "email@test.com", "login", "pass", addressDTO);

        UserCommandPort result = mapper.toCommand(dto);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Name");
        assertThat(result.email()).isEqualTo("email@test.com");
    }

    @Test
    void toCommandFromUserUpdateDTO() {
        UserWebMapper mapper = new UserWebMapper();
        UserUpdateDTO dto = new UserUpdateDTO("Name", "email@test.com", "login");

        UserCommandPort result = mapper.toCommand(dto);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Name");
    }

    @Test
    void toCommandFromAddressCreateDTO() {
        UserWebMapper mapper = new UserWebMapper();
        AddressCreateDTO dto = new AddressCreateDTO("St", 1, "City", "12345678", "Hood", "ST", null, true);

        AddressCommandPort result = mapper.toCommand(dto);

        assertThat(result).isNotNull();
        assertThat(result.streetName()).isEqualTo("St");
    }

    @Test
    void toCommandFromAddressPatchDTO() {
        UserWebMapper mapper = new UserWebMapper();
        AddressPatchDTO dto = new AddressPatchDTO("St", 1, "City", "12345678", "Hood", "ST", null, true);

        AddressCommandPort result = mapper.toCommand(dto);

        assertThat(result).isNotNull();
        assertThat(result.streetName()).isEqualTo("St");
    }
}
