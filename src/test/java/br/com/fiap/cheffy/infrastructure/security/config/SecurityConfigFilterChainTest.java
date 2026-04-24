//package br.com.fiap.cheffy.infrastructure.security.config;
//
//import br.com.fiap.cheffy.infrastructure.security.filter.JwtAuthenticationFilter;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//import java.lang.reflect.Method;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ExtendWith(MockitoExtension.class)
//class SecurityConfigFilterChainTest {
//
//    @Mock
//    private JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    @Test
//    void filterChainMethodExists() throws Exception {
//        SecurityConfig config = new SecurityConfig(jwtAuthenticationFilter);
//
//        Method method = SecurityConfig.class.getDeclaredMethod("filterChain", HttpSecurity.class);
//        assertThat(method).isNotNull();
//        assertThat(method.getReturnType()).isEqualTo(SecurityFilterChain.class);
//    }
//}
