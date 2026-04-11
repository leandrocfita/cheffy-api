package br.com.fiap.cheffy.presentation.dto;

import br.com.fiap.cheffy.presentation.validation.PostalCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RestaurantAddressCreateDTO(

        @NotBlank
        @Size(max = 255)
        String streetName,

        @NotNull
        Integer number,

        @NotBlank
        @Size(max = 255)
        String city,

        @PostalCode
        String postalCode,

        @NotBlank
        @Size(max = 255)
        String neighborhood,

        @NotBlank
        @Size(max = 2)
        String stateProvince,

        @Size(max = 255)
        String addressLine
) {
}
