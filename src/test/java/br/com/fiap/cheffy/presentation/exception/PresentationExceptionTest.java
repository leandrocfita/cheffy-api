package br.com.fiap.cheffy.presentation.exception;

import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PresentationExceptionTest {

    @Test
    void apiExceptionCreation() {
        ApiException ex = new ApiException(ExceptionsKeys.GENERIC_ERROR_MESSAGE);
        assertThat(ex.getMessage()).contains("GENERIC_ERROR_MESSAGE");
    }

    @Test
    void apiInternalServerErrorExceptionCreation() {
        ApiInternalServerErrorException ex = new ApiInternalServerErrorException(ExceptionsKeys.GENERIC_ERROR_MESSAGE);
        assertThat(ex.getMessage()).contains("GENERIC_ERROR_MESSAGE");
    }

    @Test
    void deserializationExceptionCreation() {
        DeserializationException ex = new DeserializationException(ExceptionsKeys.ERROR_ON_DESERIALIZATION);
        assertThat(ex.getMessage()).contains("ERROR_ON_DESERIALIZATION");
    }
}
