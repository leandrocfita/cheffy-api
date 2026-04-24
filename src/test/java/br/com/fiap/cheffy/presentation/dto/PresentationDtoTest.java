package br.com.fiap.cheffy.presentation.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PresentationDtoTest {

    @Test
    void createAddressCreateDTO() {
        AddressCreateDTO dto = new AddressCreateDTO("St", 1, "City", "12345678", "Hood", "ST", "Apt", true);
        
        assertThat(dto.streetName()).isEqualTo("St");
        assertThat(dto.number()).isEqualTo(1);
    }

    @Test
    void createAddressPatchDTO() {
        AddressPatchDTO dto = new AddressPatchDTO("St", 1, "City", "12345678", "Hood", "ST", "Apt", true);
        
        assertThat(dto.streetName()).isEqualTo("St");
        assertThat(dto.number()).isEqualTo(1);
    }

    @Test
    void createLoginRequestDTO() {
        LoginRequestDTO dto = new LoginRequestDTO("user", "pass");
        
        assertThat(dto.login()).isEqualTo("user");
        assertThat(dto.password()).isEqualTo("pass");
    }

    @Test
    void createTokenResponseDTO() {
        TokenResponseDTO dto = new TokenResponseDTO("token123");
        
        assertThat(dto.token()).isEqualTo("token123");
    }

    @Test
    void createUserCreateDTO() {
        AddressCreateDTO address = new AddressCreateDTO("St", 1, "City", "12345678", "Hood", "ST", null, true);
        UserCreateDTO dto = new UserCreateDTO("Name", "email@test.com", "login", "pass", address);
        
        assertThat(dto.name()).isEqualTo("Name");
        assertThat(dto.email()).isEqualTo("email@test.com");
        assertThat(dto.address()).isNotNull();
    }

    @Test
    void createUserUpdateDTO() {
        UserUpdateDTO dto = new UserUpdateDTO("Name", "email@test.com", "login");
        
        assertThat(dto.name()).isEqualTo("Name");
        assertThat(dto.email()).isEqualTo("email@test.com");
        assertThat(dto.login()).isEqualTo("login");
    }

    @Test
    void createRestaurantCreateDTO() {
        RestaurantAddressCreateDTO addr = new RestaurantAddressCreateDTO("St", 1, "City", "12345678", "Hood", "SP", null);
        RestaurantCreateDTO dto = new RestaurantCreateDTO("Name", "Brasileira", "27865757000102", null, null, "America/Sao_Paulo", false, addr);
        assertThat(dto.name()).isEqualTo("Name");
        assertThat(dto.address().streetName()).isEqualTo("St");
    }

    @Test
    void createProfileCreateReponseDto() {
        ProfileCreateReponseDto dto = new ProfileCreateReponseDto(1L, "CLIENT", "created");
        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.nameType()).isEqualTo("CLIENT");
        assertThat(dto.message()).isEqualTo("created");
    }

    @Test
    void foodItemAvailabilityDtoValidWhenAtLeastOneFieldPresent() {
        FoodItemAvailabilityDTO dto = new FoodItemAvailabilityDTO(true, null);
        assertThat(dto.isAtLeastOneFieldPresent()).isTrue();
    }

    @Test
    void foodItemAvailabilityDtoInvalidWhenBothFieldsNull() {
        FoodItemAvailabilityDTO dto = new FoodItemAvailabilityDTO(null, null);
        assertThat(dto.isAtLeastOneFieldPresent()).isFalse();
    }

    @Test
    void foodItemAvailabilityDtoValidWhenDeliveryAvailablePresent() {
        FoodItemAvailabilityDTO dto = new FoodItemAvailabilityDTO(null, false);
        assertThat(dto.isAtLeastOneFieldPresent()).isTrue();
    }
}
