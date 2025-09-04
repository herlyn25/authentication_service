package bootcamp.reto.poweup.exceptions;

import bootcamp.reto.poweup.constants.ConstantsAppsLayer;
import bootcamp.reto.poweup.model.ConstanstsModel;
import bootcamp.reto.poweup.model.user.exceptions.EmailAlreadyUsedException;
import bootcamp.reto.poweup.model.user.exceptions.InvalidCredentials;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import java.util.Map;

import bootcamp.reto.poweup.model.user.exceptions.UserValidationException;
import bootcamp.reto.poweup.r2dbc.exceptions.CustomNoFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.LinkedHashMap;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentials.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleJwtValidation(InvalidCredentials ex) {
        Map<String, Object> body = createErrorResponse(
                ConstantsAppsLayer.INVALID_CREDENTIALS,
                ex.getMessage(),
                null
        );
        return Mono.just(new ResponseEntity<>(body, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(UserValidationException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleUserValidation(UserValidationException ex) {
        Map<String, Object> body = createErrorResponse(
                ConstantsAppsLayer.FIELD_VALIDATION_ERRORS,
                ex.getMessage(),
                ex.getErrors()
        );
        return Mono.just(new ResponseEntity<>(body, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleEmailAlreadyUsed(EmailAlreadyUsedException ex) {
        Map<String, Object> body = createErrorResponse(
                ConstantsAppsLayer.EMAIL_CONFLICT,
                ex.getMessage(),
                null
        );
        return Mono.just(new ResponseEntity<>(body, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(CustomNoFoundException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleCustomException(CustomNoFoundException ex) {
        Map<String, Object> body = createErrorResponse(
                ConstantsAppsLayer.RESOURCE_NOT_FOUND,
                ex.getMessage(),
                null
        );
        return Mono.just(new ResponseEntity<>(body, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> body = createErrorResponse(
                "Invalid Arguments",
                ex.getMessage() != null ? ex.getMessage() : ConstantsAppsLayer.INVALID_PARAMETERS,
                null
        );
        return Mono.just(new ResponseEntity<>(body, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> body = createErrorResponse(
                ConstantsAppsLayer.INTERNAL_SERVER_ERROR,
                ConstantsAppsLayer.AN_UNEXPECTED_ERROR,
                null
        );
        return Mono.just(new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleGenericException(Exception ex) {
         Map<String, Object> body = createErrorResponse(
                ConstantsAppsLayer.INTERNAL_SERVER_ERROR,
                ConstantsAppsLayer.AN_UNEXPECTED_ERROR,
                null
        );
        return Mono.just(new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private Map<String, Object> createErrorResponse(String error, String message, Object details) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("error", error);
        body.put("message", message);
        return body;
    }
}