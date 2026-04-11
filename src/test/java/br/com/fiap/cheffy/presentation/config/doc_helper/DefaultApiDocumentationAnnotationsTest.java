package br.com.fiap.cheffy.presentation.config.doc_helper;

import br.com.fiap.cheffy.presentation.exceptionhandler.model.Problem;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultApiDocumentationAnnotationsTest {

    @Test
    void defaultApiErrorsShouldExposeExpectedResponses() {
        ApiResponses apiResponses = DefaultApiErrors.class.getAnnotation(ApiResponses.class);

        assertThat(apiResponses).isNotNull();
        assertThat(apiResponses.value())
                .extracting(ApiResponse::responseCode)
                .containsExactly("401", "403", "500");
        assertThat(Arrays.stream(apiResponses.value())
                .map(ApiResponse::content)
                .flatMap(Arrays::stream)
                .map(Content::mediaType))
                .containsOnly(MediaType.APPLICATION_JSON_VALUE);
        assertThat(Arrays.stream(apiResponses.value())
                .map(ApiResponse::content)
                .flatMap(Arrays::stream)
                .map(content -> content.schema().implementation()))
                .allMatch(clazz -> clazz.equals(Problem.class));
    }

    @Test
    void defaultNotFoundApiResponseShouldExposeExpectedResponse() {
        ApiResponse apiResponse = DefaultNotFoundApiResponse.class.getAnnotation(ApiResponse.class);

        assertThat(apiResponse).isNotNull();
        assertThat(apiResponse.responseCode()).isEqualTo("404");
        assertThat(apiResponse.description()).isEqualTo("Recurso não encontrado");
        assertThat(apiResponse.content()).hasSize(1);
        assertThat(apiResponse.content()[0].mediaType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(apiResponse.content()[0].schema().implementation()).isEqualTo(Problem.class);
    }
}
