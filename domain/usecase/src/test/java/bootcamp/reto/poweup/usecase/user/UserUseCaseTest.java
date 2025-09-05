package bootcamp.reto.poweup.usecase.user;

import bootcamp.reto.poweup.model.user.User;
import bootcamp.reto.poweup.model.user.exceptions.EmailAlreadyUsedException;
import bootcamp.reto.poweup.model.user.gateways.UserRepository;
import bootcamp.reto.poweup.model.role.gateways.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    private UserUseCase userUseCase;

    private User validUser;

    @BeforeEach
    void setUp() {
        userUseCase = new UserUseCase(userRepository, roleRepository);

        validUser = User.builder()
                .id(1L)
                .firstname("Juan")
                .lastname("Pérez")
                .birthdate(LocalDate.of(1990, 1, 1))
                .address("Calle 123")
                .phone(1234567890)
                .email("juan.perez@email.com")
                .password("password123")
                .documentId("12345678")
                .baseSalary(new BigDecimal("1000000"))
                .role_id(1L)
                .build();
    }

    @Test
    void save_WhenValidUserAndRoleExistsAndEmailNotUsed_ShouldSaveUserSuccessfully() {
        // Arrange
        User savedUser = validUser.toBuilder().id(1L).build();

        when(roleRepository.findRoleById(anyLong())).thenReturn(Mono.just(true));
        when(userRepository.findUserByEmail(anyString())).thenReturn(Mono.empty());
        when(userRepository.saveUser(any(User.class))).thenReturn(Mono.just(savedUser));

        // Act & Assert
        StepVerifier.create(userUseCase.save(validUser))
                .expectNext(savedUser)
                .verifyComplete();

        verify(roleRepository, times(1)).findRoleById(1L);
        verify(userRepository, times(1)).findUserByEmail("juan.perez@email.com");
        verify(userRepository, times(1)).saveUser(validUser);
    }

    @Test
    void save_WhenEmailAlreadyExists_ShouldThrowEmailAlreadyUsedException() {
        // Arrange
        User existingUser = validUser.toBuilder().id(2L).build();

        when(roleRepository.findRoleById(anyLong())).thenReturn(Mono.just(true));
        when(userRepository.findUserByEmail(anyString())).thenReturn(Mono.just(existingUser));

        // Act & Assert
        StepVerifier.create(userUseCase.save(validUser))
                .expectError(EmailAlreadyUsedException.class)
                .verify();

        verify(roleRepository, times(1)).findRoleById(1L);
        verify(userRepository, times(1)).findUserByEmail("juan.perez@email.com");
        verify(userRepository, never()).saveUser(any(User.class));
    }

    @Test
    void save_WhenRoleDoesNotExist_ShouldPropagateError() {
        // Arrange - Simular que findRoleById retorna un error
        RuntimeException roleNotFoundError = new RuntimeException("Role not found");
        when(roleRepository.findRoleById(anyLong())).thenReturn(Mono.error(roleNotFoundError));

        // Act & Assert
        StepVerifier.create(userUseCase.save(validUser))
                .expectError(RuntimeException.class)
                .verify();

        verify(roleRepository, times(1)).findRoleById(1L);
        verify(userRepository, never()).findUserByEmail(anyString());
        verify(userRepository, never()).saveUser(any(User.class));
    }

  

    @Test
    void save_WhenUserRepositoryFindByEmailFails_ShouldPropagateError() {
        // Arrange
        RuntimeException dbError = new RuntimeException("Database error");

        when(roleRepository.findRoleById(anyLong())).thenReturn(Mono.just(true));
        when(userRepository.findUserByEmail(anyString())).thenReturn(Mono.error(dbError));

        // Act & Assert
        StepVerifier.create(userUseCase.save(validUser))
                .expectError(RuntimeException.class)
                .verify();

        verify(roleRepository, times(1)).findRoleById(1L);
        verify(userRepository, times(1)).findUserByEmail("juan.perez@email.com");
        verify(userRepository, never()).saveUser(any(User.class));
    }

    @Test
    void save_WhenUserRepositorySaveFails_ShouldPropagateError() {
        // Arrange
        RuntimeException saveError = new RuntimeException("Save failed");

        when(roleRepository.findRoleById(anyLong())).thenReturn(Mono.just(true));
        when(userRepository.findUserByEmail(anyString())).thenReturn(Mono.empty());
        when(userRepository.saveUser(any(User.class))).thenReturn(Mono.error(saveError));

        // Act & Assert
        StepVerifier.create(userUseCase.save(validUser))
                .expectError(RuntimeException.class)
                .verify();

        verify(roleRepository, times(1)).findRoleById(1L);
        verify(userRepository, times(1)).findUserByEmail("juan.perez@email.com");
        verify(userRepository, times(1)).saveUser(validUser);
    }

    @Test
    void save_WhenUserValidationFails_ShouldPropagateValidationError() {
        // Arrange - User con datos inválidos (email null)
        User invalidUser = validUser.toBuilder().email(null).build();

        // Act & Assert
        StepVerifier.create(userUseCase.save(invalidUser))
                .expectError() // Esperamos que UserDomainValidation lance un error
                .verify();

        // No se debe llamar a ningún repository si la validación falla
        verify(roleRepository, never()).findRoleById(anyLong());
        verify(userRepository, never()).findUserByEmail(anyString());
        verify(userRepository, never()).saveUser(any(User.class));
    }

    @Test
    void save_WhenAllOperationsSucceed_ShouldCallMethodsInCorrectOrder() {
        // Arrange
        User savedUser = User.builder()
                .id(1L)
                .firstname(validUser.getFirstname())
                .lastname(validUser.getLastname())
                .birthdate(validUser.getBirthdate())
                .address(validUser.getAddress())
                .phone(validUser.getPhone())
                .email(validUser.getEmail())
                .password(validUser.getPassword())
                .documentId(validUser.getDocumentId())
                .baseSalary(validUser.getBaseSalary())
                .role_id(validUser.getRole_id())
                .build();

        when(roleRepository.findRoleById(anyLong())).thenReturn(Mono.just(true));
        when(userRepository.findUserByEmail(anyString())).thenReturn(Mono.empty());
        when(userRepository.saveUser(any(User.class))).thenReturn(Mono.just(savedUser));

        // Act
        StepVerifier.create(userUseCase.save(validUser))
                .expectNext(savedUser)
                .verifyComplete();

        // Assert - Verificar orden de ejecución
        var inOrder = inOrder(roleRepository, userRepository);
        inOrder.verify(roleRepository).findRoleById(1L);
        inOrder.verify(userRepository).findUserByEmail("juan.perez@email.com");
        inOrder.verify(userRepository).saveUser(validUser);
    }


}