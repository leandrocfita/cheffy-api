package br.com.fiap.cheffy.infrastructure.security.adapter;

import br.com.fiap.cheffy.domain.user.entity.AuthenticatedUser;
import br.com.fiap.cheffy.domain.user.port.input.AuthenticationManagerPort;
import br.com.fiap.cheffy.infrastructure.security.model.SpringAuthenticatedUser;
import br.com.fiap.cheffy.shared.exception.LoginFailedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.LOGIN_FAILED_EXCEPTION;

@Component
public class SpringSecurityAuthenticationManagerAdapter implements AuthenticationManagerPort {

    private final AuthenticationManager authManager;

    public SpringSecurityAuthenticationManagerAdapter(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public AuthenticatedUser authenticate(String username, String password) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            password
                    )
            );

            SpringAuthenticatedUser principal =
                    (SpringAuthenticatedUser) authentication.getPrincipal();

            return toDomain(principal);

        } catch (AuthenticationException e) {
            throw new LoginFailedException(LOGIN_FAILED_EXCEPTION, e.getMessage());
        }
    }

    private AuthenticatedUser toDomain(
            SpringAuthenticatedUser user
    ) {
        return new AuthenticatedUser(
                user.getId(),
                user.getUsername(),
                user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet())
        );
    }
}
