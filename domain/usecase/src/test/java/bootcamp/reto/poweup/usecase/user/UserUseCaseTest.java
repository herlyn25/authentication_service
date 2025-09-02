package bootcamp.reto.poweup.usecase.user;

import bootcamp.reto.poweup.model.role.gateways.RoleRepository;
import bootcamp.reto.poweup.model.user.User;
import bootcamp.reto.poweup.model.user.exceptions.EmailAlreadyUsedException;
import bootcamp.reto.poweup.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

public class UserUseCaseTest {
    private UserRepository userRepository;
    private UserUseCase userUseCase;
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp(){
        userRepository = Mockito.mock(UserRepository.class);
        userUseCase = new UserUseCase(userRepository);
    }

    private User userBuild() {
        return User.builder()
                .id(1L)
                .firstname("Erlin")
                .lastname("Castillo")
                .birthdate(LocalDate.parse("2000-07-25"))
                .address("Cra 5")
                .phone(321633)
                .email("Herly@c.cas")
                .documentId("10494094")
                .baseSalary(new BigDecimal("6457474")).build();
    }

    @Test
    void createUserWhenIsValid() {
        User user = userBuild();
        when(userRepository.findUserByEmail(user.getEmail()))
                .thenReturn(Mono.empty());
        when(userRepository.saveUser(user))
                .thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.save(user))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void notCreateUserWhenEmailExists(){
        User user = userBuild();
        when(userRepository.findUserByEmail(user.getEmail()))
                .thenReturn(Mono.just(user));
        when(userRepository.saveUser(user))
                .thenReturn(Mono.just(user));
        StepVerifier.create(userUseCase.save(user))
                .expectError(EmailAlreadyUsedException.class)
                .verify();
        verify(userRepository, never()).saveUser(any());
    }



    @Test
    void propagateErrorWhenFindUserByEmailFails() {
        User user = userBuild();

        when(userRepository.findUserByEmail(user.getEmail()))
                .thenReturn(Mono.error(new RuntimeException("DB down")));

        StepVerifier.create(userUseCase.save(user))
                .expectErrorMatches(ex -> ex instanceof RuntimeException &&
                        ex.getMessage().contains("DB down"))
                .verify();
    }
    
    @Test
    void propagateErrorWhenSaveUserFails() {
        User user = userBuild();

        when(userRepository.findUserByEmail(user.getEmail()))
                .thenReturn(Mono.empty());
        when(userRepository.saveUser(user))
                .thenReturn(Mono.error(new RuntimeException("Insert failed")));

        StepVerifier.create(userUseCase.save(user))
                .expectErrorMatches(ex -> ex instanceof RuntimeException &&
                        ex.getMessage().contains("Insert failed"))
                .verify();
    }
}
