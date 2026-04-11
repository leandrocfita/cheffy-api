//package br.com.fiap.cheffy.infrastructure.security.service;
//
//import br.com.fiap.cheffy.infrastructure.exception.TokenExpiredException;
//import br.com.fiap.cheffy.infrastructure.security.model.SpringAuthenticatedUser;
//import br.com.fiap.cheffy.infrastructure.security.properties.JwtProperties;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//
//import java.util.Set;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//class JwtServiceTest {
//
//    private JwtService jwtService;
//    private SpringAuthenticatedUser user;
//
//    @BeforeEach
//    void setUp() {
//        JwtProperties props = new JwtProperties();
//        props.setSecret("test-secret-key-with-minimum-256-bits-for-hs256-algorithm");
//        props.setExpiration(3600000L);
//        props.setIssuer("cheffy-test");
//
//        jwtService = new JwtService(props);
//
//        UUID userId = UUID.randomUUID();
//        user = new SpringAuthenticatedUser(userId, "testuser", "pass",
//            Set.of(new SimpleGrantedAuthority("ROLE_CLIENT")), true);
//    }
//
//    @Test
//    void generateTokenCreatesValidToken() {
//        String token = jwtService.generateToken(user);
//
//        assertThat(token).isNotNull();
//        assertThat(token).isNotEmpty();
//    }
//
//    @Test
//    void extractUserIdReturnsCorrectId() {
//        String token = jwtService.generateToken(user);
//
//        UUID extractedId = jwtService.extractUserId(token);
//
//        assertThat(extractedId).isEqualTo(user.getId());
//    }
//
//    @Test
//    void isTokenValidReturnsTrueForValidToken() {
//        String token = jwtService.generateToken(user);
//
//        boolean valid = jwtService.isTokenValid(token);
//
//        assertThat(valid).isTrue();
//    }
//
//    @Test
//    void isTokenValidReturnsFalseForInvalidToken() {
//        boolean valid = jwtService.isTokenValid("invalid.token.here");
//
//        assertThat(valid).isFalse();
//    }
//
//    @Test
//    void expiredTokenThrowsException() {
//        JwtProperties props = new JwtProperties();
//        props.setSecret("test-secret-key-with-minimum-256-bits-for-hs256-algorithm");
//        props.setExpiration(-1000L);
//        props.setIssuer("cheffy-test");
//
//        JwtService expiredService = new JwtService(props);
//        String token = expiredService.generateToken(user);
//
//        assertThrows(TokenExpiredException.class, () -> jwtService.extractUserId(token));
//    }
//
//    @Test
//    void tokenExpiredExceptionIsPropagated() {
//        JwtProperties props = new JwtProperties();
//        props.setSecret("test-secret-key-with-minimum-256-bits-for-hs256-algorithm");
//        props.setExpiration(1L);
//        props.setIssuer("cheffy-test");
//
//        JwtService shortLivedService = new JwtService(props);
//        String token = shortLivedService.generateToken(user);
//
//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//
//        assertThrows(TokenExpiredException.class, () -> jwtService.extractUserId(token));
//    }
//
//    @Test
//    void isTokenValidPropagatesTokenExpiredException() {
//        JwtProperties props = new JwtProperties();
//        props.setSecret("test-secret-key-with-minimum-256-bits-for-hs256-algorithm");
//        props.setExpiration(1L);
//        props.setIssuer("cheffy-test");
//
//        JwtService shortLivedService = new JwtService(props);
//        String token = shortLivedService.generateToken(user);
//
//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//
//        assertThrows(TokenExpiredException.class, () -> jwtService.isTokenValid(token));
//    }
//}
