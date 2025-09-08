package bootcamp.reto.poweup.model.user.exceptions;

import bootcamp.reto.poweup.model.ConstanstsModel;

public class InvalidCredentials extends  RuntimeException {
    public InvalidCredentials(String message) {
        super(message);
    }
}
