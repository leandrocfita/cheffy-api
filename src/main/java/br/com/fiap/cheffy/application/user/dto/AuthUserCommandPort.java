package br.com.fiap.cheffy.application.user.dto;

public record AuthUserCommandPort(
        String login,
        String password
) {
}
