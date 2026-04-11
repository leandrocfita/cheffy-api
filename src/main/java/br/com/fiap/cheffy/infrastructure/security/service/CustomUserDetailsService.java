package br.com.fiap.cheffy.infrastructure.security.service;

import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.domain.user.port.output.UserRepository;
import br.com.fiap.cheffy.infrastructure.security.model.SpringAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login)
            throws UsernameNotFoundException {

        User user = userRepository.findByLogin(login)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Invalid credentials")
                );

        return toAuthenticatedUser(user);
    }

    public SpringAuthenticatedUser loadUserById(UUID id)
            throws UsernameNotFoundException {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Invalid credentials")
                );

        return toAuthenticatedUser(user);
    }

    private SpringAuthenticatedUser toAuthenticatedUser(User user) {
        return new SpringAuthenticatedUser(
                user.getId(),
                user.getLogin(),
                user.getPassword(),
                mapProfilesToAuthorities(user),
                user.isActive()
        );
    }

    private Set<GrantedAuthority> mapProfilesToAuthorities(User user) {
        return user.getProfiles().stream()
                .map(profile ->
                        new SimpleGrantedAuthority("ROLE_" + profile.getType())
                )
                .collect(Collectors.toSet());
    }
}

