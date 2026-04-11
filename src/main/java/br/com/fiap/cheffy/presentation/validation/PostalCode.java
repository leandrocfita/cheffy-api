package br.com.fiap.cheffy.presentation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {PostalCodeValidation.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PostalCode {

    String message() default "CEP inválido";

    Class<?>[] groups() default {};

    boolean required() default true;

    Class<? extends Payload>[] payload() default {};
}
