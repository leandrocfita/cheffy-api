package br.com.fiap.cheffy.domain.user.entity;

import java.util.Set;
import java.util.UUID;

public class AuthenticatedUser {

    private final UUID id;
    private final String username;
    private final Set<String> roles;

    public AuthenticatedUser(UUID id, String username, Set<String> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Set<String> getRoles() {
        return roles;
    }
}
