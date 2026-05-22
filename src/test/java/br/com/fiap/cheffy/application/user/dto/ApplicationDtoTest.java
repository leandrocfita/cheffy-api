package br.com.fiap.cheffy.application.user.dto;

import br.com.fiap.cheffy.domain.profile.ProfileType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationDtoTest {

    @Test
    void createAddressCommandPort() {
        AddressCommandPort dto = new AddressCommandPort("St", 1, "City", "12345678", "Hood", "ST", "Apt", true);

        assertThat(dto.streetName()).isEqualTo("St");
        assertThat(dto.number()).isEqualTo(1);
        assertThat(dto.city()).isEqualTo("City");
        assertThat(dto.postalCode()).isEqualTo("12345678");
        assertThat(dto.neighborhood()).isEqualTo("Hood");
        assertThat(dto.stateProvince()).isEqualTo("ST");
        assertThat(dto.addressLine()).isEqualTo("Apt");
        assertThat(dto.main()).isTrue();
    }

    @Test
    void createAddressQueryPort() {
        AddressQueryPort dto = new AddressQueryPort(1L, "St", 1, "City", "12345678", "Hood", "ST", "Apt", true);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.streetName()).isEqualTo("St");
    }

    @Test
    void createUserCommandPort() {
        UserCommandPort dto = new UserCommandPort("Name", "email@test.com", "login", "pass", null);

        assertThat(dto.name()).isEqualTo("Name");
        assertThat(dto.email()).isEqualTo("email@test.com");
        assertThat(dto.login()).isEqualTo("login");
        assertThat(dto.password()).isEqualTo("pass");
    }

    @Test
    void createUserQueryPort() {
        UserQueryPort dto = new UserQueryPort(UUID.randomUUID().toString(),"Name", "email@test.com", true, Set.of(ProfileType.CLIENT), null);

        assertThat(dto.name()).isEqualTo("Name");
        assertThat(dto.email()).isEqualTo("email@test.com");
        assertThat(dto.active()).isTrue();
    }

    @Test
    void createLoginCommandPort() {
        LoginCommandPort dto = new LoginCommandPort("user", "pass");

        assertThat(dto.login()).isEqualTo("user");
        assertThat(dto.password()).isEqualTo("pass");
    }

    @Test
    void createLoginResultPort() {
        LoginResultPort dto = new LoginResultPort("token123");

        assertThat(dto.token()).isEqualTo("token123");
    }
}
