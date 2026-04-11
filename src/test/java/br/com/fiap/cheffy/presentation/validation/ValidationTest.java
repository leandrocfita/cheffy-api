package br.com.fiap.cheffy.presentation.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidationTest {

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private PostalCode postalCodeAnnotation;

    @Mock
    private NotBlankIfPresent notBlankAnnotation;

    private PostalCodeValidation postalCodeValidation;
    private NotBlankIfPresentValidation notBlankValidation;

    @BeforeEach
    void setUp() {
        postalCodeValidation = new PostalCodeValidation();
        notBlankValidation = new NotBlankIfPresentValidation();
    }

    @Test
    void postalCodeValidationAcceptsValidCode() {
        when(postalCodeAnnotation.required()).thenReturn(true);
        postalCodeValidation.initialize(postalCodeAnnotation);

        boolean valid = postalCodeValidation.isValid("12345678", context);

        assertThat(valid).isTrue();
    }

    @Test
    void postalCodeValidationRejectsInvalidCode() {
        when(postalCodeAnnotation.required()).thenReturn(true);
        postalCodeValidation.initialize(postalCodeAnnotation);

        boolean valid = postalCodeValidation.isValid("1234567", context);

        assertThat(valid).isFalse();
    }

    @Test
    void postalCodeValidationRejectsNull() {
        when(postalCodeAnnotation.required()).thenReturn(true);
        postalCodeValidation.initialize(postalCodeAnnotation);

        boolean valid = postalCodeValidation.isValid(null, context);

        assertThat(valid).isFalse();
    }

    @Test
    void postalCodeValidationAcceptsNullWhenNotRequired() {
        when(postalCodeAnnotation.required()).thenReturn(false);
        postalCodeValidation.initialize(postalCodeAnnotation);

        boolean valid = postalCodeValidation.isValid(null, context);

        assertThat(valid).isTrue();
    }

    @Test
    void postalCodeValidationRejectsEmpty() {
        when(postalCodeAnnotation.required()).thenReturn(true);
        postalCodeValidation.initialize(postalCodeAnnotation);

        boolean valid = postalCodeValidation.isValid("", context);

        assertThat(valid).isFalse();
    }

    @Test
    void notBlankValidationAcceptsNonBlank() {
        notBlankValidation.initialize(notBlankAnnotation);

        boolean valid = notBlankValidation.isValid("text", context);

        assertThat(valid).isTrue();
    }

    @Test
    void notBlankValidationAcceptsNull() {
        notBlankValidation.initialize(notBlankAnnotation);

        boolean valid = notBlankValidation.isValid(null, context);

        assertThat(valid).isTrue();
    }

    @Test
    void notBlankValidationRejectsBlank() {
        notBlankValidation.initialize(notBlankAnnotation);

        boolean valid = notBlankValidation.isValid("   ", context);

        assertThat(valid).isFalse();
    }
}
