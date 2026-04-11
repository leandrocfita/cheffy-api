package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.application.user.dto.LoginResultPort;
import br.com.fiap.cheffy.domain.user.port.input.LoginInput;
import br.com.fiap.cheffy.presentation.dto.LoginRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private LoginInput loginInput;

    @InjectMocks
    private AuthController authController;

    @Test
    void loginReturnsToken() {
        LoginRequestDTO request = new LoginRequestDTO("user", "pass");
        LoginResultPort result = new LoginResultPort("token123");
        when(loginInput.execute("user", "pass")).thenReturn(result);

        LoginResultPort response = authController.login(request);

        assertThat(response.token()).isEqualTo("token123");
    }
}
