package br.com.fiap.cheffy.presentation.exceptionhandler;

import br.com.fiap.cheffy.domain.profile.exception.ProfileAlreadyExistException;
import br.com.fiap.cheffy.domain.profile.exception.ProfileIsOwnerOrClientException;
import br.com.fiap.cheffy.domain.profile.exception.ProfileNotFoundException;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantNotFoundException;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantOperationNotAllowedException;
import br.com.fiap.cheffy.domain.user.exception.*;
import br.com.fiap.cheffy.infrastructure.exception.TokenExpiredException;
import br.com.fiap.cheffy.shared.exception.*;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiExceptionHandlerIntegrationTest {

    @Mock
    private MessageSource messageSource;

    @Mock
    private WebRequest webRequest;

    @InjectMocks
    private ApiExceptionHandler handler;

    @Test
    void shouldHandleUserOperationNotAllowedExceptionViaReflection() throws Exception {
        when(messageSource.getMessage(any(String.class), any(), any())).thenReturn("Error");
        
        UserOperationNotAllowedException ex = new UserOperationNotAllowedException(ExceptionsKeys.USER_MUST_HAVE_AT_LEAST_ONE_ADDRESS);
        
        var method = ApiExceptionHandler.class.getDeclaredMethod("handleUserOperationNotAllowedException", 
            UserOperationNotAllowedException.class, WebRequest.class);
        method.setAccessible(true);
        
        ResponseEntity<Object> response = (ResponseEntity<Object>) method.invoke(handler, ex, webRequest);
        
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void shouldHandleUserNotFoundExceptionViaReflection() throws Exception {
        when(messageSource.getMessage(any(String.class), any(), any())).thenReturn("User %s not found");
        
        UserNotFoundException ex = new UserNotFoundException(ExceptionsKeys.USER_NOT_FOUND_EXCEPTION, UUID.randomUUID());
        
        var method = ApiExceptionHandler.class.getDeclaredMethod("handleUserNotFoundException", 
            UserNotFoundException.class, WebRequest.class);
        method.setAccessible(true);
        
        ResponseEntity<Object> response = (ResponseEntity<Object>) method.invoke(handler, ex, webRequest);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldHandleInvalidPasswordExceptionViaReflection() throws Exception {
        when(messageSource.getMessage(any(String.class), any(), any())).thenReturn("Invalid password");
        
        InvalidPasswordException ex = new InvalidPasswordException(ExceptionsKeys.INVALID_PASSWORD_MSG, 12);
        
        var method = ApiExceptionHandler.class.getDeclaredMethod("handleInvalidPasswordException", 
            InvalidPasswordException.class, WebRequest.class);
        method.setAccessible(true);
        
        ResponseEntity<Object> response = (ResponseEntity<Object>) method.invoke(handler, ex, webRequest);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldHandleInvalidPostalCodeExceptionViaReflection() throws Exception {
        when(messageSource.getMessage(any(String.class), any(), any())).thenReturn("Invalid postal code");
        
        InvalidPostalCodeException ex = new InvalidPostalCodeException(ExceptionsKeys.INVALID_POSTAL_CODE_MSG, 8);
        
        var method = ApiExceptionHandler.class.getDeclaredMethod("handleInvalidPostalCodeException", 
            InvalidPostalCodeException.class, WebRequest.class);
        method.setAccessible(true);
        
        ResponseEntity<Object> response = (ResponseEntity<Object>) method.invoke(handler, ex, webRequest);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldHandleAddressNotFoundExceptionViaReflection() throws Exception {
        when(messageSource.getMessage(any(String.class), any(), any())).thenReturn("Address %s not found");
        
        AddressNotFoundException ex = new AddressNotFoundException(ExceptionsKeys.ADDRESS_NOT_FOUND_EXCEPTION, 1L);
        
        var method = ApiExceptionHandler.class.getDeclaredMethod("handleAddressNotFoundException", 
            AddressNotFoundException.class, WebRequest.class);
        method.setAccessible(true);
        
        ResponseEntity<Object> response = (ResponseEntity<Object>) method.invoke(handler, ex, webRequest);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldHandleProfileNotFoundExceptionViaReflection() throws Exception {
        when(messageSource.getMessage(any(String.class), any(), any())).thenReturn("Profile %s not found");
        
        ProfileNotFoundException ex = new ProfileNotFoundException(ExceptionsKeys.PROFILE_NOT_FOUND_EXCEPTION, "CLIENT");
        
        var method = ApiExceptionHandler.class.getDeclaredMethod("handleProfileNotFoundException", 
            ProfileNotFoundException.class, WebRequest.class);
        method.setAccessible(true);
        
        ResponseEntity<Object> response = (ResponseEntity<Object>) method.invoke(handler, ex, webRequest);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldHandleOperationNotAllowedExceptionViaReflection() throws Exception {
        when(messageSource.getMessage(any(String.class), any(), any())).thenReturn("Operation not allowed");
        
        OperationNotAllowedException ex = new OperationNotAllowedException(ExceptionsKeys.INVALID_OPERATION_EXCEPTION);
        
        var method = ApiExceptionHandler.class.getDeclaredMethod("handleOperationNotAllowedException", 
            OperationNotAllowedException.class, WebRequest.class);
        method.setAccessible(true);
        
        ResponseEntity<Object> response = (ResponseEntity<Object>) method.invoke(handler, ex, webRequest);
        
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void shouldHandleTokenExpiredExceptionViaReflection() throws Exception {
        when(messageSource.getMessage(any(String.class), any(), any())).thenReturn("Token expired");
        
        TokenExpiredException ex = new TokenExpiredException(ExceptionsKeys.TOKEN_EXPIRED_EXCEPTION);
        
        var method = ApiExceptionHandler.class.getDeclaredMethod("handleTokenExpiredException", 
            TokenExpiredException.class, WebRequest.class);
        method.setAccessible(true);
        
        ResponseEntity<Object> response = (ResponseEntity<Object>) method.invoke(handler, ex, webRequest);
        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void shouldHandleLoginFailedExceptionViaReflection() throws Exception {
        when(messageSource.getMessage(any(String.class), any(), any())).thenReturn("Login failed");
        
        LoginFailedException ex = new LoginFailedException(ExceptionsKeys.LOGIN_FAILED_EXCEPTION, "Original");
        
        var method = ApiExceptionHandler.class.getDeclaredMethod("handleLoginFailedException", 
            LoginFailedException.class, WebRequest.class);
        method.setAccessible(true);
        
        ResponseEntity<Object> response = (ResponseEntity<Object>) method.invoke(handler, ex, webRequest);
        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void shouldHandleRegisterFailedExceptionViaReflection() throws Exception {
        when(messageSource.getMessage(any(String.class), any(), any())).thenReturn("Register failed");
        
        RegisterFailedException ex = new RegisterFailedException(ExceptionsKeys.REGISTER_FAILED_EXCEPTION);
        
        var method = ApiExceptionHandler.class.getDeclaredMethod("handleRegisterFailedException", 
            RegisterFailedException.class, WebRequest.class);
        method.setAccessible(true);
        
        ResponseEntity<Object> response = (ResponseEntity<Object>) method.invoke(handler, ex, webRequest);
        
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void shouldHandleInvalidOperationExceptionViaReflection() throws Exception {
        when(messageSource.getMessage(any(String.class), any(), any())).thenReturn("Invalid operation");
        
        InvalidOperationException ex = new InvalidOperationException();
        
        var method = ApiExceptionHandler.class.getDeclaredMethod("handleInvalidOperationException", 
            InvalidOperationException.class, WebRequest.class);
        method.setAccessible(true);
        
        ResponseEntity<Object> response = (ResponseEntity<Object>) method.invoke(handler, ex, webRequest);
        
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void shouldHandleInvalidDataExceptionViaReflection() throws Exception {
        when(messageSource.getMessage(any(String.class), any(), any())).thenReturn("Invalid data");

        InvalidDataException ex = new InvalidDataException(ExceptionsKeys.ZONE_ID_DO_NOT_EXIST);

        var method = ApiExceptionHandler.class.getDeclaredMethod("handleInvalidDataException",
            InvalidDataException.class, WebRequest.class);
        method.setAccessible(true);

        ResponseEntity<Object> response = (ResponseEntity<Object>) method.invoke(handler, ex, webRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void shouldHandleProfileAlreadyExistException() throws Exception {
        when(messageSource.getMessage(any(String.class), any(), any())).thenReturn("Profile %s already exists");
        ProfileAlreadyExistException ex = new ProfileAlreadyExistException(ExceptionsKeys.PROFILE_ALREADY_EXIST_EXCEPTION, "CLIENT");
        ResponseEntity<Object> response = handler.handleProfileAlreadyExist(ex, webRequest);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void shouldHandleProfileIsOwnerOrClientException() throws Exception {
        when(messageSource.getMessage(any(String.class), any(), any())).thenReturn("Não é permitida a exclusão dos perfis padrão OWNER e CLIENT.");
        ProfileIsOwnerOrClientException ex = new ProfileIsOwnerOrClientException(ExceptionsKeys.PROFILE_IS_OWNER_OR_CLIENT);
        ResponseEntity<Object> response = handler.handleProfileIsOwnerOrClientException(ex, webRequest);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void shouldHandleRestaurantNotFoundExceptionViaReflection() throws Exception {
        when(messageSource.getMessage(any(String.class), any(), any())).thenReturn("Restaurant %s not found");

        RestaurantNotFoundException ex = new RestaurantNotFoundException(ExceptionsKeys.RESTAURANT_NOT_FOUND_EXCEPTION, UUID.randomUUID());

        var method = ApiExceptionHandler.class.getDeclaredMethod("handleRestaurantNotFoundException",
            RestaurantNotFoundException.class, WebRequest.class);
        method.setAccessible(true);

        ResponseEntity<Object> response = (ResponseEntity<Object>) method.invoke(handler, ex, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldHandleRestaurantOperationNotAllowedExceptionViaReflection() throws Exception {
        when(messageSource.getMessage(any(String.class), any(), any())).thenReturn("Operation not allowed");

        RestaurantOperationNotAllowedException ex = new RestaurantOperationNotAllowedException(ExceptionsKeys.RESTAURANT_USER_DOES_NOT_HAVE_OWNERSHIP_OR_IS_INACTIVE);

        var method = ApiExceptionHandler.class.getDeclaredMethod("handleRestaurantOperationNotAllowedException",
            RestaurantOperationNotAllowedException.class, WebRequest.class);
        method.setAccessible(true);

        ResponseEntity<Object> response = (ResponseEntity<Object>) method.invoke(handler, ex, webRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
}
