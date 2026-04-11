package br.com.fiap.cheffy.presentation.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Cheffy API",
                version = "v1"
        ),
        tags = {
                @Tag(name = "1. Authentication", description = "Autenticação e obtencão de token JWT"),
                @Tag(name = "2. Users", description = "Cadastro, consulta e manutenção de usuários"),
                @Tag(name = "3. Profiles", description = "Consulta e manutençâo de perfis de usuário"),
                @Tag(name = "4. Restaurants", description = "Cadastro, consulta e manutenção de restaurantes"),
                @Tag(name = "5. Food Items", description = "Cadastro e manutenção dos itens de cardápio")
        },
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfiguration {
}
