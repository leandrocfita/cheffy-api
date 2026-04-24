//package br.com.fiap.cheffy.infrastructure.security.adapter;
//
//import br.com.fiap.cheffy.domain.user.entity.AuthenticatedUser;
//import br.com.fiap.cheffy.infrastructure.security.service.JwtService;
//import br.com.fiap.cheffy.shared.exception.LoginFailedException;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//
//import java.util.Set;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class SecurityAdapterTest {
//
//    @Mock
//    private JwtService jwtService;
//
//    @Mock
//    private AuthenticationManager authManager;
//
//    @InjectMocks
//    private JwtTokenServiceAdapter jwtTokenServiceAdapter;
//
//    @InjectMocks
//    private SpringSecurityAuthenticationManagerAdapter authenticationManagerAdapter;
//
//    @Test
//    void jwtTokenServiceGeneratesToken() {
//        AuthenticatedUser user = new AuthenticatedUser(UUID.randomUUID(), "user", Set.of("ROLE_CLIENT"));
//        when(jwtService.generateToken(any())).thenReturn("token123");
//
//        String token = jwtTokenServiceAdapter.generate(user);
//
//        assertThat(token).isEqualTo("token123");
//    }
//
//    @Test
//    void authenticationManagerAuthenticatesUser() {
//        br.com.fiap.cheffy.infrastructure.security.model.SpringAuthenticatedUser springUser =
//            new br.com.fiap.cheffy.infrastructure.security.model.SpringAuthenticatedUser(
//                UUID.randomUUID(), "user", "pass", Set.of(new SimpleGrantedAuthority("ROLE_CLIENT")), true);
//        Authentication auth = new UsernamePasswordAuthenticationToken(springUser, null, springUser.getAuthorities());
//        when(authManager.authenticate(any())).thenReturn(auth);
//
//        AuthenticatedUser result = authenticationManagerAdapter.authenticate("user", "pass");
//
//        assertThat(result.getUsername()).isEqualTo("user");
//    }
//
//    @Test
//    void authenticationManagerThrowsOnBadCredentials() {
//        when(authManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));
//
//        assertThrows(LoginFailedException.class,
//            () -> authenticationManagerAdapter.authenticate("user", "wrong"));
//    }
//}
