package br.com.fiap.cheffy.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdatePasswordDTO(
        @NotBlank
        @Size(min = 12)
        String password
) {
}
