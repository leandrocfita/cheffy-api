package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.application.user.dto.LoginResultPort;
import br.com.fiap.cheffy.domain.user.port.input.LoginInput;
import br.com.fiap.cheffy.presentation.config.swagger.docs.AuthControllerDocs;
import br.com.fiap.cheffy.presentation.dto.LoginRequestDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController implements AuthControllerDocs {

    private final LoginInput loginInput;

    public AuthController(LoginInput loginInput) {

        this.loginInput = loginInput;
    }

    @Override
    @PostMapping("/login")
    public LoginResultPort login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        return loginInput.execute(loginRequestDTO.login(), loginRequestDTO.password());
    }
}
