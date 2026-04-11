package br.com.fiap.cheffy.presentation.config.swagger.docs;

import br.com.fiap.cheffy.application.user.dto.LoginResultPort;
import br.com.fiap.cheffy.presentation.config.doc_helper.DefaultPublicApiErrors;
import br.com.fiap.cheffy.presentation.dto.LoginRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;

@Tag(name = "1. Authentication", description = "Autenticação e obtenção de token JWT")
public interface AuthControllerDocs {

    @Operation(
            summary = "Autenticar usuário",
            description = "Realiza autenticação do usuário e retorna token JWT para acesso aos recursos protegidos"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Autenticação realizada com sucesso - Retorna token de acesso",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = LoginResultPort.class)
            )
    )
    @DefaultPublicApiErrors
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas - Usuário ou senha incorretos")
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = LoginRequestDTO.class),
                    examples = @ExampleObject(
                            name = "Login Request",
                            value = """
                                    {
                                      "login": "joao.silva",
                                      "password": "SenhaForte@123"
                                    }
                                    """
                    )
            )
    )
    LoginResultPort login(@Valid LoginRequestDTO loginRequestDTO);
}
