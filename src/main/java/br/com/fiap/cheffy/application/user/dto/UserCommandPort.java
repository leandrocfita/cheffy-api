package br.com.fiap.cheffy.application.user.dto;

public record UserCommandPort(

        String name,
        String email,
        String login,
        String password,
        AddressCommandPort address
) {
}

