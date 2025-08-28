package bootcamp.reto.poweup.model.user.exceptions;

public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException(String email) {
        super(email == null ? "Email is required" : String.format("Email %s already is registered", email));
    }
}
