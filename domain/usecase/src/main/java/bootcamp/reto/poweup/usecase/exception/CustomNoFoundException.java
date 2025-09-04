package bootcamp.reto.poweup.usecase.exception;

public class CustomNoFoundException extends RuntimeException{
    public CustomNoFoundException(String message) {
        super(message);
    }
}
