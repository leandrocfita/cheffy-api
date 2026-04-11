package br.com.fiap.cheffy.domain.user.entity;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticatedUserTest {

    @Test
    void createAuthenticatedUser() {
        UUID id = UUID.randomUUID();
        Set<String> roles = Set.of("CLIENT");
        
        AuthenticatedUser user = new AuthenticatedUser(id, "user", roles);
        
        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getUsername()).isEqualTo("user");
        assertThat(user.getRoles()).containsExactly("CLIENT");
    }
}
