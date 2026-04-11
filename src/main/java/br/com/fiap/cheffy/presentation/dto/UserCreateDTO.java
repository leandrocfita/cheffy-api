package br.com.fiap.cheffy.presentation.dto;

import br.com.fiap.cheffy.domain.profile.ProfileType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreateDTO(
        @NotBlank
        @Size(max = 255)
        String name,

        @NotBlank
        @Size(max = 255)
        @Email(message = "O email deve ser válido")
        String email,

        @NotBlank
        @Size(max = 255)
        String login,

        @NotBlank
        String password,

        @Valid
        @NotNull
        AddressCreateDTO address
) {
}
