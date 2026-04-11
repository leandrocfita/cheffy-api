package br.com.fiap.cheffy.domain.user.entity;

import br.com.fiap.cheffy.domain.user.exception.InvalidPasswordException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class UserPasswordValidationTest {

    @Test
    void shouldValidatePasswordSuccessfully() {
        String validPassword = "ValidPass@123";
        assertDoesNotThrow(() -> User.validatePassword(validPassword));
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsNull() {
        assertThrows(InvalidPasswordException.class, () -> User.validatePassword(null));
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsTooShort() {
        String shortPassword = "Short@1";
        assertThrows(InvalidPasswordException.class, () -> User.validatePassword(shortPassword));
    }

    @Test
    void shouldThrowExceptionWhenPasswordHasNoUppercase() {
        String noUppercase = "nouppercase@123";
        assertThrows(InvalidPasswordException.class, () -> User.validatePassword(noUppercase));
    }

    @Test
    void shouldThrowExceptionWhenPasswordHasNoLowercase() {
        String noLowercase = "NOLOWERCASE@123";
        assertThrows(InvalidPasswordException.class, () -> User.validatePassword(noLowercase));
    }

    @Test
    void shouldThrowExceptionWhenPasswordHasNoDigit() {
        String noDigit = "NoDigitPass@word";
        assertThrows(InvalidPasswordException.class, () -> User.validatePassword(noDigit));
    }

    @Test
    void shouldThrowExceptionWhenPasswordHasNoSymbol() {
        String noSymbol = "NoSymbolPass123";
        assertThrows(InvalidPasswordException.class, () -> User.validatePassword(noSymbol));
    }

    @Test
    void shouldAcceptPasswordWithExactly12Characters() {
        String exactLength = "ValidPass@12";
        assertDoesNotThrow(() -> User.validatePassword(exactLength));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "ValidPassword@123",
            "AnotherValid@456",
            "Complex!Pass123word",
            "Test@123Password",
            "MySecure#Pass789"
    })
    void shouldAcceptValidPasswords(String password) {
        assertDoesNotThrow(() -> User.validatePassword(password));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "short@1A",
            "nouppercase@123",
            "NOLOWERCASE@123",
            "NoDigitPass@word",
            "NoSymbolPass123",
            "OnlyLetters",
            "12345678901@",
            "@@@@@@@@@@@@"
    })
    void shouldRejectInvalidPasswords(String password) {
        assertThrows(InvalidPasswordException.class, () -> User.validatePassword(password));
    }

    @Test
    void shouldAcceptPasswordWithMultipleSymbols() {
        String multipleSymbols = "Valid@Pass#123!";
        assertDoesNotThrow(() -> User.validatePassword(multipleSymbols));
    }

    @Test
    void shouldAcceptPasswordWithSpaceAsSymbol() {
        String withSpace = "Valid Pass@123";
        assertDoesNotThrow(() -> User.validatePassword(withSpace));
    }

    @Test
    void shouldThrowExceptionWhenPasswordHasOnlyUppercaseAndDigits() {
        String onlyUpperAndDigits = "UPPERCASE1234567";
        assertThrows(InvalidPasswordException.class, () -> User.validatePassword(onlyUpperAndDigits));
    }

    @Test
    void shouldThrowExceptionWhenPasswordHasOnlyLowercaseAndDigits() {
        String onlyLowerAndDigits = "lowercase1234567";
        assertThrows(InvalidPasswordException.class, () -> User.validatePassword(onlyLowerAndDigits));
    }

    @Test
    void shouldThrowExceptionWhenPasswordHasOnlyLetters() {
        String onlyLetters = "OnlyLettersHere";
        assertThrows(InvalidPasswordException.class, () -> User.validatePassword(onlyLetters));
    }

    @Test
    void shouldThrowExceptionWhenPasswordHasOnlySymbolsAndDigits() {
        String onlySymbolsAndDigits = "@@@123456789";
        assertThrows(InvalidPasswordException.class, () -> User.validatePassword(onlySymbolsAndDigits));
    }

    @Test
    void shouldAcceptPasswordWithAllRequiredCharacterTypes() {
        String allTypes = "Aa1@Aa1@Aa1@";
        assertDoesNotThrow(() -> User.validatePassword(allTypes));
    }

    @Test
    void shouldThrowExceptionForEmptyString() {
        String empty = "";
        assertThrows(InvalidPasswordException.class, () -> User.validatePassword(empty));
    }

    @Test
    void shouldAcceptLongPasswordWithAllRequirements() {
        String longPassword = "ThisIsAVeryLongPassword@123WithAllRequirements";
        assertDoesNotThrow(() -> User.validatePassword(longPassword));
    }
}
