package br.com.fiap.cheffy.presentation.dto;

import br.com.fiap.cheffy.application.user.dto.AddressCommandPort;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;

import java.time.LocalTime;
import java.time.OffsetTime;

public record RestaurantCreateDTO(
        @NotBlank
        @Size(max = 255)
        String name,

        @NotBlank
        @Size(max = 255)
        String culinary,

        @NotBlank
        @CNPJ(message = "O CNPJ deve estar em um formato válido")
        String cnpj,

        @JsonFormat(pattern = "HH:mm")
        LocalTime openingTime,

        @JsonFormat(pattern = "HH:mm")
        LocalTime closingTime,

        //ex: "America/Sao_Paulo"
        @NotBlank
        @Size(max =50)
        String zoneId,

        @NotNull
        boolean open24hours,

        @NotNull
        @Valid
        RestaurantAddressCreateDTO address
) {
}
