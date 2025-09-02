package bootcamp.reto.poweup.exceptions;

import bootcamp.reto.poweup.model.user.exceptions.EmailAlreadyUsedException;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import java.util.Map;

import bootcamp.reto.poweup.model.user.exceptions.UserValidationException;
import bootcamp.reto.poweup.r2dbc.exceptions.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.LinkedHashMap;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserValidationException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleUserValidation(UserValidationException ex) {
        Map<String, Object> body = createErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                ex.getMessage(),
                ex.getErrors()
        );
        return Mono.just(new ResponseEntity<>(body, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleEmailAlreadyUsed(EmailAlreadyUsedException ex) {
        Map<String, Object> body = createErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Email Conflict",
                ex.getMessage(),
                null
        );
        return Mono.just(new ResponseEntity<>(body, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(CustomException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleCustomException(CustomException ex) {
        Map<String, Object> body = createErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Resource Not Found",
                ex.getMessage(),
                null
        );
        return Mono.just(new ResponseEntity<>(body, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> body = createErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Arguments",
                ex.getMessage() != null ? ex.getMessage() : "Invalid request parameters",
                null
        );
        return Mono.just(new ResponseEntity<>(body, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleRuntimeException(RuntimeException ex) {
        // Log the error for debugging
        ex.printStackTrace();

        Map<String, Object> body = createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred",
                null
        );
        return Mono.just(new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleGenericException(Exception ex) {
        // Log the error for debugging
        ex.printStackTrace();

        Map<String, Object> body = createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred",
                null
        );
        return Mono.just(new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private Map<String, Object> createErrorResponse(int status, String error, String message, Object details) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status);
        body.put("error", error);
        body.put("message", message);
        if (details != null) {
            body.put("details", details);
        }
        return body;
    }
}