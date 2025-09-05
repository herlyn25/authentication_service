package bootcamp.reto.poweup.r2dbc.exceptions;

public class CustomNoFoundException extends RuntimeException{
    public CustomNoFoundException(String message) {
        super(message);
    }
}
