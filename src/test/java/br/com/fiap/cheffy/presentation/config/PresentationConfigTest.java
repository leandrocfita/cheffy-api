package br.com.fiap.cheffy.presentation.config;

import br.com.fiap.cheffy.presentation.config.swagger.OpenApiConfiguration;
import br.com.fiap.cheffy.presentation.config.swagger.SwaggerConfig;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.customizers.OperationCustomizer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PresentationConfigTest {

    @Test
    void openApiConfigurationCreatesBean() {
        OpenApiConfiguration config = new OpenApiConfiguration();
        
        assertThat(config).isNotNull();
    }

    @Test
    void swaggerConfigCreatesOpenAPI() {
        SwaggerConfig config = new SwaggerConfig();
        
        OpenAPI openAPI = config.openApiSpec();
        
        assertThat(openAPI).isNotNull();
        assertThat(openAPI.getComponents()).isNotNull();
    }

    @Test
    void swaggerConfigCreatesOperationCustomizer() {
        SwaggerConfig config = new SwaggerConfig();
        
        OperationCustomizer customizer = config.operationCustomizer();
        
        assertThat(customizer).isNotNull();
    }

    @Test
    void jacksonConfigCreatesBean() {
        JacksonConfig config = new JacksonConfig();
        
        assertThat(config).isNotNull();
    }
}
