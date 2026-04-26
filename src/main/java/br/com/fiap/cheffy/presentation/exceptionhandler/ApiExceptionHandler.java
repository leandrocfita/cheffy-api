package br.com.fiap.cheffy.presentation.exceptionhandler;

import br.com.fiap.cheffy.domain.fooditem.exception.FoodItemAlreadyExistInRestaurant;
import br.com.fiap.cheffy.domain.fooditem.exception.FoodItemDoesNotExist;
import br.com.fiap.cheffy.domain.fooditem.exception.FoodItemNotFoundException;
import br.com.fiap.cheffy.domain.fooditem.exception.FoodItemUnavailableForOrderException;
import br.com.fiap.cheffy.domain.profile.exception.ProfileAlreadyExistException;
import br.com.fiap.cheffy.domain.profile.exception.ProfileIsOwnerOrClientException;
import br.com.fiap.cheffy.domain.profile.exception.ProfileNotFoundException;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantInactiveException;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantNotFoundException;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantOperationNotAllowedException;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantDoesNotExistException;
import br.com.fiap.cheffy.domain.user.exception.*;
import br.com.fiap.cheffy.infrastructure.exception.TokenExpiredException;
import br.com.fiap.cheffy.presentation.exception.ApiInternalServerErrorException;
import br.com.fiap.cheffy.presentation.exception.DeserializationException;
import br.com.fiap.cheffy.presentation.exceptionhandler.model.Problem;
import br.com.fiap.cheffy.shared.exception.*;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    public ApiExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private static final String GENERIC_ERROR_MESSAGE = ExceptionsKeys.GENERIC_ERROR_MESSAGE.toString();
    private static final String ARGUMENT_NOT_VALID_ERROR = ExceptionsKeys.ARGUMENT_NOT_VALID_ERROR.toString();
    private static final String ERROR_ON_DESERIALIZATION = ExceptionsKeys.ERROR_ON_DESERIALIZATION.toString();
    private static final String INVALID_FORMAT_ERROR = ExceptionsKeys.INVALID_FORMAT_ERROR.toString();
    private static final String PROPERTY_BINDING_ERROR = ExceptionsKeys.PROPERTY_BINDING_ERROR.toString();
    private static final String PROFILE_ALREADY_EXIST = ExceptionsKeys.PROFILE_ALREADY_EXIST_EXCEPTION.toString();

    static class ApiExceptionHandlerService {

    }

    @ExceptionHandler(InvalidDataException.class)
    private ResponseEntity<Object> handleInvalidDataException(InvalidDataException ex, WebRequest request) {

        String title = getExceptionName(ex);
        String message = getMessage(GENERIC_ERROR_MESSAGE);
        String detail = getMessage(ex.getMessage());

        HttpStatus httpStatusCode = HttpStatus.UNAUTHORIZED;

        Problem problem = createProblemBuilder(
                httpStatusCode,
                title,
                detail)
                .userMessage(message)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);

    }

    @ExceptionHandler(RestaurantOperationNotAllowedException.class)
    private ResponseEntity<Object> handleRestaurantOperationNotAllowedException(RestaurantOperationNotAllowedException ex, WebRequest request) {
        String message = getMessage(ex.getMessage());

        HttpStatus httpStatusCode = HttpStatus.CONFLICT;

        Problem problem = createProblemBuilder(
                httpStatusCode,
                getExceptionName(ex),
                message)
                .userMessage(message)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);
    }

    @ExceptionHandler(UserOperationNotAllowedException.class)
    private ResponseEntity<Object> handleUserOperationNotAllowedException(UserOperationNotAllowedException ex, WebRequest request) {

        String title = getExceptionName(ex);
        String message = getMessage(ex.getMessage());

        HttpStatus httpStatusCode = HttpStatus.CONFLICT;

        Problem problem = createProblemBuilder(
                httpStatusCode,
                title,
                message)
                .userMessage(message)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);

    }

    @ExceptionHandler(RestaurantNotFoundException.class)
    private ResponseEntity<Object> handleRestaurantNotFoundException(RestaurantNotFoundException ex, WebRequest request) {
        String message = getMessage(ex.getMessage());
        message = String.format(message, ex.getId().toString());

        HttpStatus httpStatusCode = HttpStatus.NOT_FOUND;

        Problem problem = createProblemBuilder(
                httpStatusCode,
                getExceptionName(ex),
                message)
                .userMessage(message)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {

        String title = getExceptionName(ex);
        String message = getMessage(ex.getMessage());
        message = String.format(message, ex.getId().toString());

        HttpStatus httpStatusCode = HttpStatus.NOT_FOUND;

        Problem problem = createProblemBuilder(
                httpStatusCode,
                title,
                message)
                .userMessage(message)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);

    }

    @ExceptionHandler(InvalidPasswordException.class)
    private ResponseEntity<Object> handleInvalidPasswordException(InvalidPasswordException ex, WebRequest request) {

        String title = getExceptionName(ex);
        String message = getMessage(ex.getMessage());
        message = String.format(message, ex.getMinPasswordLength());

        HttpStatus httpStatusCode = HttpStatus.BAD_REQUEST;

        Problem problem = createProblemBuilder(
                httpStatusCode,
                title,
                message)
                .userMessage(message)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);

    }

    @ExceptionHandler(InvalidPostalCodeException.class)
    private ResponseEntity<Object> handleInvalidPostalCodeException(InvalidPostalCodeException ex, WebRequest request) {
        String title = getExceptionName(ex);

        String message = getMessage(ex.getMessage());
        message = String.format(message, ex.getMinPostalCodeLength());

        HttpStatus httpStatusCode = HttpStatus.BAD_REQUEST;

        Problem problem = createProblemBuilder(
                httpStatusCode,
                title,
                message)
                .userMessage(message)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);
    }

    @ExceptionHandler(AddressNotFoundException.class)
    private ResponseEntity<Object> handleAddressNotFoundException(AddressNotFoundException ex, WebRequest request) {

        String title = getExceptionName(ex);
        String message = getMessage(ex.getMessage());

        message = String.format(message, ex.getId());

        HttpStatus httpStatusCode = HttpStatus.NOT_FOUND;

        Problem problem = createProblemBuilder(
                httpStatusCode,
                title,
                message)
                .userMessage(message)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);

    }

    @ExceptionHandler(ProfileNotFoundException.class)
    private ResponseEntity<Object> handleProfileNotFoundException(ProfileNotFoundException ex, WebRequest request) {

        String title = getExceptionName(ex);
        String message = getMessage(ex.getMessage());
        message = String.format(message, ex.getType());

        HttpStatus httpStatusCode = HttpStatus.NOT_FOUND;

        Problem problem = createProblemBuilder(
                httpStatusCode,
                title,
                message)
                .userMessage(message)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);

    }

    @ExceptionHandler(OperationNotAllowedException.class)
    private ResponseEntity<Object> handleOperationNotAllowedException(OperationNotAllowedException ex, WebRequest request) {

        String title = getExceptionName(ex);
        String message = getMessage(ex.getMessage());

        HttpStatus httpStatusCode = HttpStatus.CONFLICT;

        Problem problem = createProblemBuilder(
                httpStatusCode,
                title,
                message)
                .userMessage(message)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);

    }

    @ExceptionHandler(TokenExpiredException.class)
    private ResponseEntity<Object> handleTokenExpiredException(TokenExpiredException ex, WebRequest request) {

        String title = getExceptionName(ex);
        String message = getMessage(ex.getMessage());

        HttpStatus httpStatusCode = HttpStatus.UNAUTHORIZED;

        Problem problem = createProblemBuilder(
                httpStatusCode,
                title,
                message)
                .userMessage(getMessage(GENERIC_ERROR_MESSAGE))
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);

    }

    @ExceptionHandler(LoginFailedException.class)
    private ResponseEntity<Object> handleLoginFailedException(LoginFailedException ex, WebRequest request) {

        String title = getExceptionName(ex);
        String message = getMessage(ex.getMessage());
        String detail = ex.getOriginalMessage();

        HttpStatus httpStatusCode = HttpStatus.UNAUTHORIZED;

        Problem problem = createProblemBuilder(
                httpStatusCode,
                title,
                detail)
                .userMessage(message)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);

    }

    @ExceptionHandler(RegisterFailedException.class)
    private ResponseEntity<Object> handleRegisterFailedException(RegisterFailedException ex, WebRequest request) {

        String title = getExceptionName(ex);
        String message = getMessage(ex.getMessage());

        HttpStatus httpStatusCode = HttpStatus.CONFLICT;

        Problem problem = createProblemBuilder(
                httpStatusCode,
                title,
                message)
                .userMessage(message)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);

    }

    @ExceptionHandler(ProfileAlreadyExistException.class)
    public ResponseEntity<Object> handleProfileAlreadyExist(ProfileAlreadyExistException ex, WebRequest request) {

        String title = getExceptionName(ex);
        String message = getMessage(ex.getMessage());

        message = String.format(message, ex.getType());

        HttpStatus httpStatusCode = HttpStatus.CONFLICT;

        Problem problem = createProblemBuilder(httpStatusCode, title, message).userMessage(message).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);
    }

    @ExceptionHandler(ProfileIsOwnerOrClientException.class)
    public ResponseEntity<Object> handleProfileIsOwnerOrClientException(ProfileIsOwnerOrClientException ex, WebRequest request) {
        String title = getExceptionName(ex);
        String message = getMessage(ex.getMessage());

        HttpStatus httpStatusCode = HttpStatus.CONFLICT;

        Problem problem = createProblemBuilder(httpStatusCode, title, message).userMessage(message).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);
    }

    @ExceptionHandler(RestaurantInactiveException.class)
    private ResponseEntity<Object> handleRestaurantInactiveException(RestaurantInactiveException ex, WebRequest request) {
        String message = getMessage(ex.getMessage());
        message = String.format(message, ex.getId().toString());

        HttpStatus httpStatusCode = HttpStatus.UNPROCESSABLE_ENTITY;

        Problem problem = createProblemBuilder(
                httpStatusCode,
                getExceptionName(ex),
                message)
                .userMessage(message)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);
    }

    @ExceptionHandler(RestaurantDoesNotExistException.class)
    public ResponseEntity<Object> handleRestaurantDoesNotExist(RestaurantDoesNotExistException ex, WebRequest request) {

        String title = getExceptionName(ex);
        String message = getMessage(ex.getMessage());

        message = String.format(message, ex.getType());

        HttpStatus httpStatusCode = HttpStatus.NOT_FOUND;

        Problem problem = createProblemBuilder(httpStatusCode, title, message).userMessage(message).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);
    }

    @ExceptionHandler(FoodItemAlreadyExistInRestaurant.class)
    public ResponseEntity<Object> handleFoodItemAlreadyExistInRestaurant(FoodItemAlreadyExistInRestaurant ex, WebRequest request) {
        String title = getExceptionName(ex);
        String message = getMessage(ex.getMessage());

        message = String.format(message, ex.getType());

        HttpStatus httpStatusCode = HttpStatus.CONFLICT;

        Problem problem = createProblemBuilder(httpStatusCode, title, message).userMessage(message).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);
    }

    @ExceptionHandler(FoodItemDoesNotExist.class)
    public ResponseEntity<Object> handleFoodItemDoesNotExist(FoodItemDoesNotExist ex, WebRequest request) {
        String title = getExceptionName(ex);
        String message = getMessage(ex.getMessage());

        message = String.format(message, ex.getType());

        HttpStatus httpStatusCode = HttpStatus.NOT_FOUND;

        Problem problem = createProblemBuilder(httpStatusCode, title, message).userMessage(message).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);
    }

    @ExceptionHandler(FoodItemUnavailableForOrderException.class)
    public ResponseEntity<Object> handleFoodItemUnavailableForOrderException(FoodItemUnavailableForOrderException ex, WebRequest request) {
        String title = getExceptionName(ex);
        String message = getMessage(ex.getMessage());

        message = String.format(message, ex.getId());

        HttpStatus httpStatusCode = HttpStatus.CONFLICT;

        Problem problem = createProblemBuilder(httpStatusCode, title, message).userMessage(message).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);
    }

    @Override
    @Nullable
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof InvalidFormatException invalidFormatException) {
            return handleInvalidFormat(invalidFormatException, headers, status, request);
        } else if (rootCause instanceof PropertyBindingException propertyBindingException) {
            return handlePropertyBinding(propertyBindingException, headers, status, request);
        }

        String detail = getMessage(ERROR_ON_DESERIALIZATION);

        Problem problem = createProblemBuilder(
                status,
                DeserializationException.class.getSimpleName(),
                detail)
                .userMessage(getMessage(GENERIC_ERROR_MESSAGE))
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex,
                                                         HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String path = joinPath(ex.getPath());

        String detail = String.format(getMessage(PROPERTY_BINDING_ERROR),
                path, ex.getReferringClass().getSimpleName());

        Problem problem = createProblemBuilder(
                status,
                DeserializationException.class.getSimpleName(),
                detail)
                .userMessage(getMessage(GENERIC_ERROR_MESSAGE))
                .build();


        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex,
                                                       HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String path = joinPath(ex.getPath());

        String detail = String.format(getMessage(INVALID_FORMAT_ERROR),
                path, ex.getValue(), ex.getTargetType().getSimpleName());

        Problem problem = createProblemBuilder(
                status,
                DeserializationException.class.getSimpleName(),
                detail)
                .userMessage(getMessage(GENERIC_ERROR_MESSAGE))
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @ExceptionHandler(FoodItemNotFoundException.class)
    public ResponseEntity<Object> handleFoodItemNotFoundException(FoodItemNotFoundException ex, WebRequest request) {
        String title = getExceptionName(ex);
        String message = getMessage(ex.getMessage());
        message = String.format(message, ex.getId().toString());

        HttpStatus httpStatusCode = HttpStatus.NOT_FOUND;

        Problem problem = createProblemBuilder(
                httpStatusCode,
                title,
                message)
                .userMessage(message)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);
    }

    @Override
    @Nullable
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String userMessage = getMessage(ARGUMENT_NOT_VALID_ERROR);
        String detail = getMessage(ERROR_ON_DESERIALIZATION);

        List<Problem.Field> problemFields = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldErrors -> {
                    String message = messageSource.getMessage(fieldErrors, LocaleContextHolder.getLocale());

                    return Problem.Field.builder()
                            .name(fieldErrors.getField())
                            .userMessage(message)
                            .build();
                }).toList();

        HttpStatus exceptionStatus = HttpStatus.BAD_REQUEST;


        Problem problem = createProblemBuilder(
                exceptionStatus,
                DeserializationException.class.getSimpleName(),
                detail)
                .userMessage(userMessage)
                .fields(problemFields)
                .build();

        return handleExceptionInternal(ex, problem, headers, exceptionStatus, request);
    }

    @ExceptionHandler(InvalidOperationException.class)
    private ResponseEntity<Object> handleInvalidOperationException(InvalidOperationException ex, WebRequest request) {

        String title = getExceptionName(ex);
        String message = getMessage(ex.getMessage());

        HttpStatus httpStatusCode = HttpStatus.CONFLICT;

        Problem problem = createProblemBuilder(
                httpStatusCode,
                title,
                message)
                .userMessage(message)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatusCode, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {

        String detail = getMessage(GENERIC_ERROR_MESSAGE);

        Problem problem = createProblemBuilder(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ApiInternalServerErrorException.class.getSimpleName(),
                detail)
                .userMessage(getMessage(GENERIC_ERROR_MESSAGE))
                .build();

        log.error("EXCEÇÃO NÃO TRATADA: {}", String.valueOf(ex));


        return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);

    }

    @Override
    @Nullable
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers,
                                                             HttpStatusCode status, WebRequest request) {

        if (body == null) {
            body = Problem.builder()
                    .status(status.value())
                    .title(ex.getClass().getSimpleName())
                    .userMessage(getMessage(GENERIC_ERROR_MESSAGE))
                    .build();

            log.error("######### Exceção com Problem nulo: {}", String.valueOf(ex));

        } else if (body instanceof String string) {
            body = Problem.builder()
                    .status(status.value())
                    .title(string)
                    .userMessage(getMessage(GENERIC_ERROR_MESSAGE))
                    .build();
        }

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private String joinPath(List<JsonMappingException.Reference> references) {
        return references.stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining("."));
    }

    private Problem.ProblemBuilder createProblemBuilder(HttpStatusCode status,
                                                        String title, String detail) {
        return Problem.builder()
                .status(status.value())
                .title(title)
                .timestamp(LocalDateTime.now())
                .detail(detail);
    }

    private static String getExceptionName(Exception ex) {
        return ex.getClass().getSimpleName();
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, Locale.getDefault());
    }


}
