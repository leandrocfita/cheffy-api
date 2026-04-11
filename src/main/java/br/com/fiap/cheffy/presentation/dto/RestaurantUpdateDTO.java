package br.com.fiap.cheffy.presentation.dto;

import br.com.fiap.cheffy.presentation.validation.NotBlankIfPresent;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;

public record RestaurantUpdateDTO(
        @NotBlankIfPresent
        @Size(max = 255)
        String name,

        @NotBlankIfPresent
        @Size(max = 255)
        String culinary,

        @JsonFormat(pattern = "HH:mm")
        LocalTime openingTime,

        @JsonFormat(pattern = "HH:mm")
        LocalTime closingTime,

        @NotBlankIfPresent
        @Size(max = 50)
        String zoneId,

        Boolean open24hours
) {
}