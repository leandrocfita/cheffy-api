//package br.com.fiap.cheffy.application.user.usecase;
//
//import br.com.fiap.cheffy.application.user.dto.LoginResultPort;
//import br.com.fiap.cheffy.domain.user.entity.AuthenticatedUser;
//import br.com.fiap.cheffy.domain.user.port.input.AuthenticationManagerPort;
//import br.com.fiap.cheffy.domain.user.port.input.TokenGeneratorPort;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Set;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class LoginUseCaseTest {
//
//    @Mock
//    private AuthenticationManagerPort authenticationManager;
//
//    @Mock
//    private TokenGeneratorPort tokenGenerator;
//
//    private LoginUseCase loginUseCase;
//
//    @BeforeEach
//    void setUp() {
//        loginUseCase = new LoginUseCase(authenticationManager, tokenGenerator);
//    }
//
//    @Test
//    void executeAuthenticatesAndReturnsToken() {
//        String login = "jane.doe";
//        String password = "ValidPass1!";
//        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
//                UUID.randomUUID(),
//                login,
//                Set.of("ROLE_USER")
//        );
//
//        when(authenticationManager.authenticate(login, password)).thenReturn(authenticatedUser);
//        when(tokenGenerator.generate(authenticatedUser)).thenReturn("token-value");
//
//        LoginResultPort result = loginUseCase.execute(login, password);
//
//        assertEquals("token-value", result.token());
//        ArgumentCaptor<AuthenticatedUser> userCaptor = ArgumentCaptor.forClass(AuthenticatedUser.class);
//        verify(tokenGenerator).generate(userCaptor.capture());
//        assertEquals(authenticatedUser, userCaptor.getValue());
//    }
//
//    @Test
//    void executePropagatesAuthenticationFailure() {
//        String login = "jane.doe";
//        String password = "InvalidPass!";
//        RuntimeException failure = new RuntimeException("auth failed");
//
//        when(authenticationManager.authenticate(login, password)).thenThrow(failure);
//
//        RuntimeException thrown = assertThrows(RuntimeException.class, () -> loginUseCase.execute(login, password));
//
//        assertEquals(failure, thrown);
//        verify(tokenGenerator, never()).generate(any(AuthenticatedUser.class));
//    }
//}
