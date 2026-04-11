package br.com.fiap.cheffy.presentation.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserUpdatePasswordDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateDTOWithValidPassword() {
        UserUpdatePasswordDTO dto = new UserUpdatePasswordDTO("ValidPassword@123");
        
        assertNotNull(dto);
        assertEquals("ValidPassword@123", dto.password());
    }

    @Test
    void shouldPassValidationWithValidPassword() {
        UserUpdatePasswordDTO dto = new UserUpdatePasswordDTO("ValidPassword@123");
        
        Set<ConstraintViolation<UserUpdatePasswordDTO>> violations = validator.validate(dto);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenPasswordIsNull() {
        UserUpdatePasswordDTO dto = new UserUpdatePasswordDTO(null);
        
        Set<ConstraintViolation<UserUpdatePasswordDTO>> violations = validator.validate(dto);
        
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("must not be blank") || v.getMessage().contains("não deve estar em branco")));
    }

    @Test
    void shouldFailValidationWhenPasswordIsEmpty() {
        UserUpdatePasswordDTO dto = new UserUpdatePasswordDTO("");
        
        Set<ConstraintViolation<UserUpdatePasswordDTO>> violations = validator.validate(dto);
        
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenPasswordIsTooShort() {
        UserUpdatePasswordDTO dto = new UserUpdatePasswordDTO("Short@1");
        
        Set<ConstraintViolation<UserUpdatePasswordDTO>> violations = validator.validate(dto);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void shouldPassValidationWithExactly12Characters() {
        UserUpdatePasswordDTO dto = new UserUpdatePasswordDTO("ValidPass@12");
        
        Set<ConstraintViolation<UserUpdatePasswordDTO>> violations = validator.validate(dto);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldPassValidationWithLongPassword() {
        UserUpdatePasswordDTO dto = new UserUpdatePasswordDTO("ThisIsAVeryLongPassword@123");
        
        Set<ConstraintViolation<UserUpdatePasswordDTO>> violations = validator.validate(dto);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenPasswordIsBlank() {
        UserUpdatePasswordDTO dto = new UserUpdatePasswordDTO("   ");
        
        Set<ConstraintViolation<UserUpdatePasswordDTO>> violations = validator.validate(dto);
        
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldBeRecordWithCorrectAccessor() {
        String password = "TestPassword@123";
        UserUpdatePasswordDTO dto = new UserUpdatePasswordDTO(password);
        
        assertEquals(password, dto.password());
    }

    @Test
    void shouldHaveCorrectEquality() {
        UserUpdatePasswordDTO dto1 = new UserUpdatePasswordDTO("SamePassword@123");
        UserUpdatePasswordDTO dto2 = new UserUpdatePasswordDTO("SamePassword@123");
        
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void shouldHaveCorrectInequality() {
        UserUpdatePasswordDTO dto1 = new UserUpdatePasswordDTO("Password1@123");
        UserUpdatePasswordDTO dto2 = new UserUpdatePasswordDTO("Password2@456");
        
        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldHaveCorrectToString() {
        UserUpdatePasswordDTO dto = new UserUpdatePasswordDTO("TestPassword@123");
        
        String toString = dto.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("UserUpdatePasswordDTO"));
    }
}
