package br.com.fiap.cheffy.presentation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PostalCodeValidation implements ConstraintValidator<PostalCode, String> {

    private static final String POSTAL_CODE_REGEX = "\\d{8}";

    private boolean required;

    @Override
    public void initialize(PostalCode constraintAnnotation) {
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        if (!required && value == null) {
            return true;
        }


        if (value == null) {
            return false;
        }

        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return false;
        }
        return trimmed.matches(POSTAL_CODE_REGEX);
    }
}
