package br.com.fiap.cheffy.presentation.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OpenApiConfigurationTest {

    @Test
    void shouldDeclareOpenApiDefinitionMetadata() {
        OpenAPIDefinition definition = OpenApiConfiguration.class.getAnnotation(OpenAPIDefinition.class);

        assertThat(definition).isNotNull();
        assertThat(definition.info().title()).isEqualTo("Cheffy API");
        assertThat(definition.info().version()).isEqualTo("v1");
        assertThat(definition.security())
                .extracting(SecurityRequirement::name)
                .containsExactly("bearerAuth");
    }

    @Test
    void shouldDeclareBearerSecurityScheme() {
        SecurityScheme securityScheme = OpenApiConfiguration.class.getAnnotation(SecurityScheme.class);

        assertThat(securityScheme).isNotNull();
        assertThat(securityScheme.name()).isEqualTo("bearerAuth");
        assertThat(securityScheme.type()).isEqualTo(SecuritySchemeType.HTTP);
        assertThat(securityScheme.scheme()).isEqualTo("bearer");
        assertThat(securityScheme.bearerFormat()).isEqualTo("JWT");
        assertThat(securityScheme.in()).isEqualTo(SecuritySchemeIn.HEADER);
    }
}
