package br.com.fiap.cheffy.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(

        @NotBlank
        String login,

        @NotBlank
        String password
) {
}
