package br.com.fiap.cheffy.presentation.config;

import br.com.fiap.cheffy.presentation.config.swagger.SwaggerConfig;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.junit.jupiter.api.Test;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.web.method.HandlerMethod;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class SwaggerConfigTest {

    @Test
    void openApiSpecCreatesBean() {
        SwaggerConfig config = new SwaggerConfig();

        OpenAPI openAPI = config.openApiSpec();

        assertThat(openAPI).isNotNull();
        assertThat(openAPI.getComponents()).isNotNull();
        assertThat(openAPI.getComponents().getSchemas()).containsKey("ApiErrorResponse");
        assertThat(openAPI.getComponents().getSchemas()).containsKey("ApiFieldError");
    }

    @Test
    void operationCustomizerCreatesBean() {
        SwaggerConfig config = new SwaggerConfig();

        OperationCustomizer customizer = config.operationCustomizer();

        assertThat(customizer).isNotNull();
    }

    @Test
    void operationCustomizerAddsErrorResponse() {
        SwaggerConfig config = new SwaggerConfig();
        OperationCustomizer customizer = config.operationCustomizer();

        Operation operation = new Operation();
        operation.setResponses(new ApiResponses());
        HandlerMethod handlerMethod = mock(HandlerMethod.class);

        Operation result = customizer.customize(operation, handlerMethod);

        assertThat(result.getResponses()).containsKey("4xx/5xx");
    }
}
