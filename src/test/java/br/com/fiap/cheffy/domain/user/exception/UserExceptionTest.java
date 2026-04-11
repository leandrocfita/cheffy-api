package br.com.fiap.cheffy.domain.user.exception;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.*;
import static org.assertj.core.api.Assertions.assertThat;

class UserExceptionTest {

    @Test
    void addressNotFoundExceptionWithId() {
        AddressNotFoundException ex = new AddressNotFoundException(ADDRESS_NOT_FOUND_EXCEPTION, 1L);
        assertThat(ex.getMessage()).contains("ADDRESS_NOT_FOUND_EXCEPTION");
        assertThat(ex.getId()).isEqualTo(1L);
    }

    @Test
    void invalidPasswordException() {
        InvalidPasswordException ex = new InvalidPasswordException(INVALID_PASSWORD_MSG, 12);
        assertThat(ex.getMessage()).contains("INVALID_PASSWORD_MSG");
        assertThat(ex.getMinPasswordLength()).isEqualTo(12);
    }

    @Test
    void invalidPostalCodeException() {
        InvalidPostalCodeException ex = new InvalidPostalCodeException(INVALID_POSTAL_CODE_MSG, 8);
        assertThat(ex.getMessage()).contains("INVALID_POSTAL_CODE_MSG");
        assertThat(ex.getMinPostalCodeLength()).isEqualTo(8);
    }

    @Test
    void userNotFoundException() {
        UUID id = UUID.randomUUID();
        UserNotFoundException ex = new UserNotFoundException(USER_NOT_FOUND_EXCEPTION, id);
        assertThat(ex.getMessage()).contains("USER_NOT_FOUND_EXCEPTION");
        assertThat(ex.getId()).isEqualTo(id);
    }

    @Test
    void userOperationNotAllowedException() {
        UserOperationNotAllowedException ex = new UserOperationNotAllowedException(USER_MUST_HAVE_AT_LEAST_ONE_ADDRESS);
        assertThat(ex.getMessage()).contains("USER_MUST_HAVE_AT_LEAST_ONE_ADDRESS");
    }
}
