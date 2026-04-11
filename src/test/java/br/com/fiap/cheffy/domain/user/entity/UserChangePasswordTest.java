package br.com.fiap.cheffy.domain.user.entity;

import br.com.fiap.cheffy.domain.user.exception.InvalidPasswordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserChangePasswordTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(UUID.randomUUID(), "John Doe", "john@email.com", "john.doe", "oldEncodedPassword", true);
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        String newPassword = "NewValidPass@123";
        
        user.changePassword(newPassword);
        
        assertEquals(newPassword, user.getPassword());
    }

    @Test
    void shouldThrowExceptionWhenChangingToInvalidPassword() {
        String invalidPassword = "short";
        
        assertThrows(InvalidPasswordException.class, () -> user.changePassword(invalidPassword));
    }

    @Test
    void shouldThrowExceptionWhenChangingToNullPassword() {
        assertThrows(InvalidPasswordException.class, () -> user.changePassword(null));
    }

    @Test
    void shouldNotChangePasswordWhenValidationFails() {
        String originalPassword = user.getPassword();
        String invalidPassword = "invalid";
        
        assertThrows(InvalidPasswordException.class, () -> user.changePassword(invalidPassword));
        assertEquals(originalPassword, user.getPassword());
    }

    @Test
    void shouldChangePasswordMultipleTimes() {
        String firstPassword = "FirstValid@123";
        String secondPassword = "SecondValid@456";
        
        user.changePassword(firstPassword);
        assertEquals(firstPassword, user.getPassword());
        
        user.changePassword(secondPassword);
        assertEquals(secondPassword, user.getPassword());
    }

    @Test
    void shouldValidatePasswordBeforeChanging() {
        String passwordWithoutUppercase = "nouppercase@123";
        
        assertThrows(InvalidPasswordException.class, () -> user.changePassword(passwordWithoutUppercase));
    }

    @Test
    void shouldValidatePasswordWithoutLowercase() {
        String passwordWithoutLowercase = "NOLOWERCASE@123";
        
        assertThrows(InvalidPasswordException.class, () -> user.changePassword(passwordWithoutLowercase));
    }

    @Test
    void shouldValidatePasswordWithoutDigit() {
        String passwordWithoutDigit = "NoDigitPass@word";
        
        assertThrows(InvalidPasswordException.class, () -> user.changePassword(passwordWithoutDigit));
    }

    @Test
    void shouldValidatePasswordWithoutSymbol() {
        String passwordWithoutSymbol = "NoSymbolPass123";
        
        assertThrows(InvalidPasswordException.class, () -> user.changePassword(passwordWithoutSymbol));
    }

    @Test
    void shouldValidatePasswordTooShort() {
        String shortPassword = "Short@1";
        
        assertThrows(InvalidPasswordException.class, () -> user.changePassword(shortPassword));
    }
}
