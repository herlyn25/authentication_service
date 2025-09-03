package bootcamp.reto.poweup.r2dbc.exceptions;

public class ExpiratedTokenException extends RuntimeException {

    public ExpiratedTokenException(String message) {
        super(message);
    }
}
