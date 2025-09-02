package bootcamp.reto.poweup.model.user.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class UserValidationException extends RuntimeException{
    private final List<String> errors;

    public UserValidationException(List<String> errors) {
        super(String.join(", ", errors));
        this.errors = errors;
    }
}