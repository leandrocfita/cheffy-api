package br.com.fiap.cheffy.application.user.dto;

public record LoginCommandPort(
        String login,
        String password
) {
}
