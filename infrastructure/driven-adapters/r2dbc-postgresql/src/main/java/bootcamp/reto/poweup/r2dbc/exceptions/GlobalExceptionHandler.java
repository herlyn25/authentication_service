package bootcamp.reto.poweup.r2dbc.exceptions;

import java.util.*;

import bootcamp.reto.poweup.model.user.exceptions.EmailAlreadyUsedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;


public class GlobalExceptionHandler extends RuntimeException {
    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleMissingParams(IllegalArgumentException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", ex.getMessage());
        return Mono.just(new ResponseEntity<>(body, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleEmailAlreadyUsed(EmailAlreadyUsedException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", ex.getMessage());
        return Mono.just(new ResponseEntity<>(body, HttpStatus.CONFLICT));
    }
}