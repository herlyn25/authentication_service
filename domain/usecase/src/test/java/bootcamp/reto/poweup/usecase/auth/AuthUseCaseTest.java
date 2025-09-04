package bootcamp.reto.poweup.usecase.auth;

import bootcamp.reto.poweup.model.auth.AuthRequest;
import bootcamp.reto.poweup.model.auth.gateways.JwtTokenRepository;
import bootcamp.reto.poweup.model.user.User;
import bootcamp.reto.poweup.model.user.exceptions.InvalidCredentials;
import bootcamp.reto.poweup.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthUseCaseTest {

    @Mock
    private JwtTokenRepository jwtTokenRepository;

    @Mock
    private UserRepository userRepository;

    private AuthUseCase authUseCase;

    private AuthRequest validAuthRequest;
    private User validUser;
    private String validToken;

    @BeforeEach
    void setUp() {
        authUseCase = new AuthUseCase(jwtTokenRepository, userRepository);

        validAuthRequest = AuthRequest.builder()
                .email("user@example.com")
                .password("password123")
                .build();

        validUser = User.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .birthdate(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .phone(1234567890)
                .email("user@example.com")
                .password("hashedPassword")
                .documentId("12345678")
                .baseSalary(new BigDecimal("50000"))
                .role_id(1L)
                .build();

        validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwicm9sZSI6MSwiaWF0IjoxNTE2MjM5MDIyfQ.abc123";
    }

    // === LOGIN TESTS ===

    @Test
    void login_WhenCredentialsAreValid_ShouldReturnAuthResponse() {
        // Arrange
        when(userRepository.authenticateUser(anyString(), anyString()))
                .thenReturn(Mono.just(validUser));
        when(jwtTokenRepository.generateToken(anyString(), anyLong()))
                .thenReturn(Mono.just(validToken));

        // Act & Assert
        StepVerifier.create(authUseCase.login(validAuthRequest))
                .assertNext(authResponse -> {
                    assertThat(authResponse.getToken()).isEqualTo(validToken);
                    assertThat(authResponse.getTokenType()).isEqualTo("Bearer");
                    assertThat(authResponse.getEmail()).isEqualTo(validUser.getEmail());
                    assertThat(authResponse.getExpiresIn()).isEqualTo(bootcamp.reto.poweup.usecase.ConstantsUUC.EXPIRATION_TIME_TOKEN);
                })
                .verifyComplete();

        verify(userRepository, times(1)).authenticateUser("user@example.com", "password123");
        verify(jwtTokenRepository, times(1)).generateToken("user@example.com", 1L);
    }

    @Test
    void login_WhenCredentialsAreInvalid_ShouldPropagateAuthenticationError() {
        // Arrange
        InvalidCredentials authError = new InvalidCredentials("Invalid credentials");
        when(userRepository.authenticateUser(anyString(), anyString()))
                .thenReturn(Mono.error(authError));

        // Act & Assert
        StepVerifier.create(authUseCase.login(validAuthRequest))
                .expectError(InvalidCredentials.class)
                .verify();

        verify(userRepository, times(1)).authenticateUser("user@example.com", "password123");
        verify(jwtTokenRepository, never()).generateToken(anyString(), anyLong());
    }

    @Test
    void login_WhenUserAuthenticationSucceedsButTokenGenerationFails_ShouldPropagateTokenError() {
        // Arrange
        RuntimeException tokenError = new RuntimeException("Token generation failed");
        when(userRepository.authenticateUser(anyString(), anyString()))
                .thenReturn(Mono.just(validUser));
        when(jwtTokenRepository.generateToken(anyString(), anyLong()))
                .thenReturn(Mono.error(tokenError));

        // Act & Assert
        StepVerifier.create(authUseCase.login(validAuthRequest))
                .expectError(RuntimeException.class)
                .verify();

        verify(userRepository, times(1)).authenticateUser("user@example.com", "password123");
        verify(jwtTokenRepository, times(1)).generateToken("user@example.com", 1L);
    }

    @Test
    void login_WhenUserRepositoryReturnsEmpty_ShouldCompleteEmpty() {
        // Arrange
        when(userRepository.authenticateUser(anyString(), anyString()))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(authUseCase.login(validAuthRequest))
                .verifyComplete();

        verify(userRepository, times(1)).authenticateUser("user@example.com", "password123");
        verify(jwtTokenRepository, never()).generateToken(anyString(), anyLong());
    }

    @Test
    void login_WithDifferentUserData_ShouldGenerateTokenWithCorrectParameters() {
        // Arrange
        User differentUser = validUser.toBuilder()
                .email("admin@example.com")
                .role_id(2L)
                .build();

        AuthRequest differentRequest = validAuthRequest.toBuilder()
                .email("admin@example.com")
                .password("adminpass")
                .build();

        when(userRepository.authenticateUser("admin@example.com", "adminpass"))
                .thenReturn(Mono.just(differentUser));
        when(jwtTokenRepository.generateToken("admin@example.com", 2L))
                .thenReturn(Mono.just("different-token"));

        // Act & Assert
        StepVerifier.create(authUseCase.login(differentRequest))
                .assertNext(authResponse -> {
                    assertThat(authResponse.getToken()).isEqualTo("different-token");
                    assertThat(authResponse.getEmail()).isEqualTo("admin@example.com");
                })
                .verifyComplete();

        verify(userRepository, times(1)).authenticateUser("admin@example.com", "adminpass");
        verify(jwtTokenRepository, times(1)).generateToken("admin@example.com", 2L);
    }

    @Test
    void login_ShouldCallRepositoriesInCorrectOrder() {
        // Arrange
        when(userRepository.authenticateUser(anyString(), anyString()))
                .thenReturn(Mono.just(validUser));
        when(jwtTokenRepository.generateToken(anyString(), anyLong()))
                .thenReturn(Mono.just(validToken));

        // Act
        StepVerifier.create(authUseCase.login(validAuthRequest))
                .expectNextCount(1)
                .verifyComplete();

        // Assert - Verify execution order
        var inOrder = inOrder(userRepository, jwtTokenRepository);
        inOrder.verify(userRepository).authenticateUser("user@example.com", "password123");
        inOrder.verify(jwtTokenRepository).generateToken("user@example.com", 1L);
    }

    @Test
    void validateToken_WhenTokenIsValid_ShouldReturnTrue() {
        // Arrange
        String token = "valid-token";
        when(jwtTokenRepository.validateToken(token)).thenReturn(Mono.just(true));

        // Act & Assert
        StepVerifier.create(authUseCase.validateToken(token))
                .expectNext(true)
                .verifyComplete();

        verify(jwtTokenRepository, times(1)).validateToken(token);
    }

    @Test
    void validateToken_WhenTokenIsInvalid_ShouldReturnFalse() {
        // Arrange
        String token = "invalid-token";
        when(jwtTokenRepository.validateToken(token)).thenReturn(Mono.just(false));

        // Act & Assert
        StepVerifier.create(authUseCase.validateToken(token))
                .expectNext(false)
                .verifyComplete();

        verify(jwtTokenRepository, times(1)).validateToken(token);
    }

    @Test
    void validateToken_WhenTokenIsExpired_ShouldReturnFalse() {
        // Arrange
        String expiredToken = "expired-token";
        when(jwtTokenRepository.validateToken(expiredToken)).thenReturn(Mono.just(false));

        // Act & Assert
        StepVerifier.create(authUseCase.validateToken(expiredToken))
                .expectNext(false)
                .verifyComplete();

        verify(jwtTokenRepository, times(1)).validateToken(expiredToken);
    }

    @Test
    void validateToken_WhenRepositoryThrowsException_ShouldPropagateError() {
        // Arrange
        String token = "problematic-token";
        RuntimeException validationError = new RuntimeException("Token validation failed");
        when(jwtTokenRepository.validateToken(token)).thenReturn(Mono.error(validationError));

        // Act & Assert
        StepVerifier.create(authUseCase.validateToken(token))
                .expectError(RuntimeException.class)
                .verify();

        verify(jwtTokenRepository, times(1)).validateToken(token);
    }

    @Test
    void validateToken_WhenTokenIsNull_ShouldCallRepositoryWithNull() {
        // Arrange
        when(jwtTokenRepository.validateToken(null)).thenReturn(Mono.just(false));

        // Act & Assert
        StepVerifier.create(authUseCase.validateToken(null))
                .expectNext(false)
                .verifyComplete();

        verify(jwtTokenRepository, times(1)).validateToken(null);
    }

    @Test
    void validateToken_WhenTokenIsEmpty_ShouldCallRepositoryWithEmptyString() {
        // Arrange
        String emptyToken = "";
        when(jwtTokenRepository.validateToken(emptyToken)).thenReturn(Mono.just(false));

        // Act & Assert
        StepVerifier.create(authUseCase.validateToken(emptyToken))
                .expectNext(false)
                .verifyComplete();

        verify(jwtTokenRepository, times(1)).validateToken(emptyToken);
    }

    @Test
    void validateToken_WithMultipleDifferentTokens_ShouldValidateEachCorrectly() {
        // Arrange
        String validToken1 = "valid-token-1";
        String validToken2 = "valid-token-2";
        String invalidToken = "invalid-token";

        when(jwtTokenRepository.validateToken(validToken1)).thenReturn(Mono.just(true));
        when(jwtTokenRepository.validateToken(validToken2)).thenReturn(Mono.just(true));
        when(jwtTokenRepository.validateToken(invalidToken)).thenReturn(Mono.just(false));

        // Act & Assert
        StepVerifier.create(authUseCase.validateToken(validToken1))
                .expectNext(true)
                .verifyComplete();

        StepVerifier.create(authUseCase.validateToken(validToken2))
                .expectNext(true)
                .verifyComplete();

        StepVerifier.create(authUseCase.validateToken(invalidToken))
                .expectNext(false)
                .verifyComplete();

        verify(jwtTokenRepository, times(1)).validateToken(validToken1);
        verify(jwtTokenRepository, times(1)).validateToken(validToken2);
        verify(jwtTokenRepository, times(1)).validateToken(invalidToken);
    }
}