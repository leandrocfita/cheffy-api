package br.com.fiap.cheffy.presentation.dto;

import br.com.fiap.cheffy.presentation.validation.NotBlankIfPresent;
import br.com.fiap.cheffy.presentation.validation.PostalCode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AddressPatchDTO(

        @NotBlankIfPresent
        @Size(max = 255, min = 3)
        String streetName,

        @Positive
        @Min(1)
        Integer number,

        @NotBlankIfPresent
        @Size(max = 255, min = 3)
        String city,

        @PostalCode(required = false)
        String postalCode,

        @NotBlankIfPresent
        @Size(max = 255, min = 3)
        String neighborhood,

        @NotBlankIfPresent
        @Size(max = 2)
        String stateProvince,

        @NotBlankIfPresent
        @Size(max = 255, min = 3)
        String addressLine,

        Boolean main
) {
}
