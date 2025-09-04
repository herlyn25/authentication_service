package bootcamp.reto.poweup.usecase.role;

import bootcamp.reto.poweup.model.role.gateways.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleUseCaseTest {

    @Mock
    private RoleRepository roleRepository;

    private RoleUseCase roleUseCase;

    @BeforeEach
    void setUp() {
        roleUseCase = new RoleUseCase(roleRepository);
    }

    @Test
    void findRoleByCode_WhenRoleExists_ShouldReturnTrue() {
        // Arrange
        Long roleId = 1L;
        when(roleRepository.findRoleById(roleId)).thenReturn(Mono.just(true));

        // Act & Assert
        StepVerifier.create(roleUseCase.findRoleByCode(roleId))
                .expectNext(true)
                .verifyComplete();

        verify(roleRepository, times(1)).findRoleById(roleId);
    }

    @Test
    void findRoleByCode_WhenRoleDoesNotExist_ShouldReturnFalse() {
        // Arrange
        Long roleId = 999L;
        when(roleRepository.findRoleById(roleId)).thenReturn(Mono.just(false));

        // Act & Assert
        StepVerifier.create(roleUseCase.findRoleByCode(roleId))
                .expectNext(false)
                .verifyComplete();

        verify(roleRepository, times(1)).findRoleById(roleId);
    }

    @Test
    void findRoleByCode_WhenRepositoryThrowsException_ShouldPropagateError() {
        // Arrange
        Long roleId = 1L;
        RuntimeException repositoryError = new RuntimeException("Database connection failed");
        when(roleRepository.findRoleById(roleId)).thenReturn(Mono.error(repositoryError));

        // Act & Assert
        StepVerifier.create(roleUseCase.findRoleByCode(roleId))
                .expectError(RuntimeException.class)
                .verify();

        verify(roleRepository, times(1)).findRoleById(roleId);
    }

    @Test
    void findRoleByCode_WhenRepositoryReturnsEmpty_ShouldCompleteEmpty() {
        // Arrange
        Long roleId = 1L;
        when(roleRepository.findRoleById(roleId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(roleUseCase.findRoleByCode(roleId))
                .verifyComplete();

        verify(roleRepository, times(1)).findRoleById(roleId);
    }

    @Test
    void findRoleByCode_WhenIdIsNull_ShouldCallRepositoryWithNull() {
        // Arrange
        Long nullId = null;
        when(roleRepository.findRoleById(nullId)).thenReturn(Mono.just(false));

        // Act & Assert
        StepVerifier.create(roleUseCase.findRoleByCode(nullId))
                .expectNext(false)
                .verifyComplete();

        verify(roleRepository, times(1)).findRoleById(nullId);
    }

    @Test
    void findRoleByCode_WhenIdIsNullAndRepositoryThrowsError_ShouldPropagateError() {
        // Arrange
        Long nullId = null;
        IllegalArgumentException argumentError = new IllegalArgumentException("ID cannot be null");
        when(roleRepository.findRoleById(nullId)).thenReturn(Mono.error(argumentError));

        // Act & Assert
        StepVerifier.create(roleUseCase.findRoleByCode(nullId))
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(roleRepository, times(1)).findRoleById(nullId);
    }

    @Test
    void findRoleByCode_WithDifferentIds_ShouldCallRepositoryWithCorrectParameters() {
        // Arrange
        Long roleId1 = 1L;
        Long roleId2 = 2L;
        Long roleId3 = 100L;

        when(roleRepository.findRoleById(roleId1)).thenReturn(Mono.just(true));
        when(roleRepository.findRoleById(roleId2)).thenReturn(Mono.just(false));
        when(roleRepository.findRoleById(roleId3)).thenReturn(Mono.just(true));

        // Act & Assert - Test multiple calls
        StepVerifier.create(roleUseCase.findRoleByCode(roleId1))
                .expectNext(true)
                .verifyComplete();

        StepVerifier.create(roleUseCase.findRoleByCode(roleId2))
                .expectNext(false)
                .verifyComplete();

        StepVerifier.create(roleUseCase.findRoleByCode(roleId3))
                .expectNext(true)
                .verifyComplete();

        // Verify each call was made with correct parameters
        verify(roleRepository, times(1)).findRoleById(roleId1);
        verify(roleRepository, times(1)).findRoleById(roleId2);
        verify(roleRepository, times(1)).findRoleById(roleId3);
        verify(roleRepository, times(3)).findRoleById(anyLong());
    }

    @Test
    void findRoleByCode_WhenRepositoryReturnsUnexpectedError_ShouldPropagateSpecificError() {
        // Arrange
        Long roleId = 1L;
        IllegalStateException stateError = new IllegalStateException("Repository in invalid state");
        when(roleRepository.findRoleById(roleId)).thenReturn(Mono.error(stateError));

        // Act & Assert
        StepVerifier.create(roleUseCase.findRoleByCode(roleId))
                .expectError(IllegalStateException.class)
                .verify();

        verify(roleRepository, times(1)).findRoleById(roleId);
    }

}
