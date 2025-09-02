package bootcamp.reto.poweup.r2dbc.exceptions;

public class CustomException extends RuntimeException{
    public CustomException(Long id, String entity) {
        super(String.format("%s with id %s no exists", entity, id));
    }

    public CustomException(Integer id, String entity) {
        super(String.format("%s with Document Id %s no exists", entity, id));
    }

    public CustomException(String email) {
        super("The user with email " + email + " no exists");
    }
}
