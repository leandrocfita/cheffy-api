package br.com.fiap.cheffy.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProfileCreateReponseDto(
        @JsonProperty("Id")
        Long id,
        @JsonProperty("name")
        String nameType,
        @JsonProperty("message")
        String message
) {
}
