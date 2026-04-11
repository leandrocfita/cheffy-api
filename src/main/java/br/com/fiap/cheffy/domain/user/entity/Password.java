package br.com.fiap.cheffy.domain.user.entity;

import br.com.fiap.cheffy.domain.user.exception.InvalidPasswordException;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.INVALID_PASSWORD_MSG;

public class Password {

    private static final int MIN_PASSWORD_LENGTH = 12; //TODO parametrizar

    private String value;

    public Password(String value) {
        validatePassword(value);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static void validatePassword(String password) {

        if (password == null) {
            throw new InvalidPasswordException(
                    INVALID_PASSWORD_MSG, MIN_PASSWORD_LENGTH
            );
        }

        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSymbol = password.chars().anyMatch(
                ch -> !Character.isLetterOrDigit(ch)
        );

        if (!(password.length() >= MIN_PASSWORD_LENGTH
                && hasUppercase
                && hasLowercase
                && hasDigit
                && hasSymbol)) {
            throw new InvalidPasswordException(
                    INVALID_PASSWORD_MSG, MIN_PASSWORD_LENGTH);
        }
    }
}
