package br.com.fiap.cheffy.presentation.validation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ValidationAnnotationTest {

    @Test
    void postalCodeAnnotationExists() throws NoSuchFieldException {
        PostalCode annotation = TestClass.class.getDeclaredField("postalCode").getAnnotation(PostalCode.class);
        
        assertThat(annotation).isNotNull();
        assertThat(annotation.required()).isTrue();
    }

    @Test
    void notBlankIfPresentAnnotationExists() throws NoSuchFieldException {
        NotBlankIfPresent annotation = TestClass.class.getDeclaredField("name").getAnnotation(NotBlankIfPresent.class);
        
        assertThat(annotation).isNotNull();
    }

    static class TestClass {
        @PostalCode(required = true)
        private String postalCode;

        @NotBlankIfPresent
        private String name;
    }
}
