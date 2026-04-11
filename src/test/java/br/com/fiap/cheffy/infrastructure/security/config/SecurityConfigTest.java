package br.com.fiap.cheffy.infrastructure.security.config;

import br.com.fiap.cheffy.infrastructure.security.filter.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private AuthenticationConfiguration authConfig;

    @Mock
    private AuthenticationManager authManager;

    @Test
    void passwordEncoderCreatesBean() {
        SecurityConfig config = new SecurityConfig(jwtAuthenticationFilter);

        PasswordEncoder encoder = config.passwordEncoder();

        assertThat(encoder).isNotNull();
    }

    @Test
    void authenticationManagerCreatesBean() throws Exception {
        SecurityConfig config = new SecurityConfig(jwtAuthenticationFilter);
        when(authConfig.getAuthenticationManager()).thenReturn(authManager);

        AuthenticationManager manager = config.authenticationManager(authConfig);

        assertThat(manager).isEqualTo(authManager);
    }

    @Test
    void filterChainCreatesBean() {
        SecurityConfig config = new SecurityConfig(jwtAuthenticationFilter);
        // Apenas verifica se o método existe
        try {
            SecurityConfig.class.getDeclaredMethod("filterChain", HttpSecurity.class);
        } catch (NoSuchMethodException e) {
            throw new AssertionError("filterChain method not found");
        }
    }
}
