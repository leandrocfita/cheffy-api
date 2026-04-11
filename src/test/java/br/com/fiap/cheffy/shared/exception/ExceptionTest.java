package br.com.fiap.cheffy.shared.exception;

import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionTest {

    @Test
    void businessExceptionCreation() {
        BusinessException ex = new BusinessException(ExceptionsKeys.GENERIC_ERROR_MESSAGE);
        assertThat(ex.getMessage()).contains("GENERIC_ERROR_MESSAGE");
    }

    @Test
    void invalidOperationExceptionCreation() {
        InvalidOperationException ex = new InvalidOperationException();
        assertThat(ex.getMessage()).contains("INVALID_OPERATION_EXCEPTION");
    }

    @Test
    void loginFailedExceptionCreation() {
        LoginFailedException ex = new LoginFailedException(ExceptionsKeys.LOGIN_FAILED_EXCEPTION, "original");
        assertThat(ex.getMessage()).contains("LOGIN_FAILED_EXCEPTION");
        assertThat(ex.getOriginalMessage()).isEqualTo("original");
    }

    @Test
    void operationNotAllowedExceptionCreation() {
        OperationNotAllowedException ex = new OperationNotAllowedException(ExceptionsKeys.INVALID_OPERATION_EXCEPTION);
        assertThat(ex.getMessage()).contains("INVALID_OPERATION_EXCEPTION");
    }

    @Test
    void registerFailedExceptionCreation() {
        RegisterFailedException ex = new RegisterFailedException(ExceptionsKeys.REGISTER_FAILED_EXCEPTION);
        assertThat(ex.getMessage()).contains("REGISTER_FAILED_EXCEPTION");
    }
}
