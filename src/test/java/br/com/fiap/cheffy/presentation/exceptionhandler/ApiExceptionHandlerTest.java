package br.com.fiap.cheffy.presentation.exceptionhandler;

import br.com.fiap.cheffy.presentation.exceptionhandler.model.Problem;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiExceptionHandlerTest {

    @Mock
    private MessageSource messageSource;

    @Mock
    private WebRequest webRequest;

    @InjectMocks
    private ApiExceptionHandler handler;

    @Test
    void shouldHandleHttpMessageNotReadableWithInvalidFormat() {
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Error message");
        
        InvalidFormatException cause = mock(InvalidFormatException.class);
        when(cause.getPath()).thenReturn(List.of());
        when(cause.getValue()).thenReturn("invalid");
        when(cause.getTargetType()).thenReturn((Class) String.class);
        
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("msg", cause, null);
        ResponseEntity<Object> response = handler.handleHttpMessageNotReadable(ex, null, HttpStatus.BAD_REQUEST, webRequest);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldHandleHttpMessageNotReadableWithPropertyBinding() {
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Error message");
        
        UnrecognizedPropertyException cause = mock(UnrecognizedPropertyException.class);
        when(cause.getPath()).thenReturn(List.of());
        when(cause.getReferringClass()).thenReturn((Class) String.class);
        
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("msg", cause, null);
        ResponseEntity<Object> response = handler.handleHttpMessageNotReadable(ex, null, HttpStatus.BAD_REQUEST, webRequest);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldHandleHttpMessageNotReadableGeneric() {
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Error message");
        
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("msg");
        ResponseEntity<Object> response = handler.handleHttpMessageNotReadable(ex, null, HttpStatus.BAD_REQUEST, webRequest);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldHandleMethodArgumentNotValid() {
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Validation error");
        when(messageSource.getMessage(any(FieldError.class), any())).thenReturn("Field error");
        
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "field", "error");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
        ResponseEntity<Object> response = handler.handleMethodArgumentNotValid(ex, null, HttpStatus.BAD_REQUEST, webRequest);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Problem problem = (Problem) response.getBody();
        assertNotNull(problem);
        assertFalse(problem.getFields().isEmpty());
    }

    @Test
    void shouldHandleGenericException() {
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Internal error");
        
        Exception ex = new Exception("Generic error");
        ResponseEntity<Object> response = handler.handleUncaught(ex, webRequest);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void shouldHandleExceptionInternalWithNullBody() {
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Error");
        
        Exception ex = new Exception("Test");
        ResponseEntity<Object> response = handler.handleExceptionInternal(ex, null, null, HttpStatus.BAD_REQUEST, webRequest);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldHandleExceptionInternalWithStringBody() {
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Error");
        
        Exception ex = new Exception("Test");
        ResponseEntity<Object> response = handler.handleExceptionInternal(ex, "String body", null, HttpStatus.BAD_REQUEST, webRequest);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Problem);
    }

    @Test
    void shouldHandleExceptionInternalWithProblemBody() {
        Problem problem = Problem.builder().status(400).title("Test").userMessage("User message").build();
        Exception ex = new Exception("Test");
        ResponseEntity<Object> response = handler.handleExceptionInternal(ex, problem, null, HttpStatus.BAD_REQUEST, webRequest);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(problem, response.getBody());
    }
}
