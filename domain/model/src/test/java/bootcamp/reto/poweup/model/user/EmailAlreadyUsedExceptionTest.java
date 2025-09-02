package bootcamp.reto.poweup.model.user;

import bootcamp.reto.poweup.model.user.exceptions.EmailAlreadyUsedException;
import bootcamp.reto.poweup.model.user.gateways.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@DisplayName("User Exception Test")
public class EmailAlreadyUsedExceptionTest {
    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("EmailAlreadyUsedException debería contener email en el mensaje")
    void emailAlreadyUsedExceptionShouldContainEmail() {
        String email = "test@example.com";
        EmailAlreadyUsedException exception = new EmailAlreadyUsedException(email);
        Assertions.assertThat(exception.getMessage()).contains(email);
    }

    void emailAlreadyNoUsedException(String email) {
        String email2 = "prueba@test.com";
        EmailAlreadyUsedException exception = new EmailAlreadyUsedException(email2);
        Assertions.assertThat(!(exception.getMessage()).contains(email2));
    }
}