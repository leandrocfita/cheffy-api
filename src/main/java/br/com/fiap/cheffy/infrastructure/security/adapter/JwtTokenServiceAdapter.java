package br.com.fiap.cheffy.infrastructure.security.adapter;

import br.com.fiap.cheffy.domain.user.entity.AuthenticatedUser;
import br.com.fiap.cheffy.domain.user.port.input.TokenGeneratorPort;
import br.com.fiap.cheffy.infrastructure.security.model.SpringAuthenticatedUser;
import br.com.fiap.cheffy.infrastructure.security.service.JwtService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class JwtTokenServiceAdapter implements TokenGeneratorPort {

    private final JwtService jwtService;

    public JwtTokenServiceAdapter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public String generate(AuthenticatedUser user) {
        return jwtService.generateToken(
                new SpringAuthenticatedUser(
                        user.getId(),
                        user.getUsername(),
                        null,
                        user.getRoles().stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toSet()),
                        true
                )
        );
    }
}
