package br.com.fiap.cheffy.presentation.dto;

import br.com.fiap.cheffy.presentation.validation.NotBlankIfPresent;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO (

        @NotBlankIfPresent
        @Size(min = 3, max = 255)
        String name,

        @NotBlankIfPresent
        @Email(message = "O email deve ser válido")
        String email,

        @NotBlankIfPresent
        @Size(min = 3, max = 255)
        String login
) {
}
