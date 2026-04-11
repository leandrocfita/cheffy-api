package br.com.fiap.cheffy.infrastructure.security.filter;

import br.com.fiap.cheffy.infrastructure.exception.TokenExpiredException;
import br.com.fiap.cheffy.infrastructure.security.model.SpringAuthenticatedUser;
import br.com.fiap.cheffy.infrastructure.security.service.CustomUserDetailsService;
import br.com.fiap.cheffy.infrastructure.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private HandlerExceptionResolver exceptionResolver;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter filter;

    @Test
    void filterPassesWhenNoAuthHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).extractUserId(any());
    }

    @Test
    void filterPassesWhenAuthHeaderNotBearer() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Basic xyz");

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).extractUserId(any());
    }

    @Test
    void filterAuthenticatesWithValidToken() throws Exception {
        UUID userId = UUID.randomUUID();
        String token = "valid.jwt.token";
        SpringAuthenticatedUser user = new SpringAuthenticatedUser(userId, "user", "pass", 
            Set.of(new SimpleGrantedAuthority("ROLE_CLIENT")), true);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUserId(token)).thenReturn(userId);
        when(userDetailsService.loadUserById(userId)).thenReturn(user);
        when(jwtService.isTokenValid(token)).thenReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void filterHandlesTokenExpiredException() throws Exception {
        String token = "expired.jwt.token";
        TokenExpiredException exception = mock(TokenExpiredException.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUserId(token)).thenThrow(exception);

        filter.doFilterInternal(request, response, filterChain);

        verify(exceptionResolver).resolveException(request, response, null, exception);
        verify(filterChain, never()).doFilter(any(), any());
    }
}
