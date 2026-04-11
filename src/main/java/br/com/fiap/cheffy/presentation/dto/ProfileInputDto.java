package br.com.fiap.cheffy.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record ProfileInputDto(
        @NotBlank
        @JsonProperty("name")
        String profileNameType
) {
}
