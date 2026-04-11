package br.com.fiap.cheffy.infrastructure.security.model;

import java.util.UUID;

public record CurrentUser(
        UUID id,
        String login
) {}