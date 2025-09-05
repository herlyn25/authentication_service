package bootcamp.reto.poweup.model.user;

import bootcamp.reto.poweup.model.user.exceptions.UserValidationException;
import bootcamp.reto.poweup.model.user.validations.UserDomainValidation;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

class UserValidationExceptionTest {
    @Test
    void validateUser_whenUserIsValid_shouldReturnUser() {
        // Arrange
        User validUser = new User();
        validUser.setFirstname("John");
        validUser.setLastname("Doe");
        validUser.setEmail("john.doe@example.com");
        validUser.setDocumentId("123456789");
        validUser.setBirthdate(LocalDate.of(1990, 1, 1));
        validUser.setAddress("123 Main St");
        validUser.setBaseSalary(new BigDecimal("5000000"));

        // Actualiza el Mono User
        Mono<User> result = UserDomainValidation.validateUser(validUser);

        // Assert
        StepVerifier.create(result)
                .expectNext(validUser)
                .verifyComplete();
    }

    @Test
    void validateUser_whenUserIsEmpty_shouldThrowValidationException() {
        // Arrange
        User emptyUser = new User(); // Todos los campos nulos

        // Act
        Mono<User> result = UserDomainValidation.validateUser(emptyUser);

        // Assert
        StepVerifier.create(result)
                .expectErrorSatisfies(throwable -> {
                    assert throwable instanceof UserValidationException;
                    UserValidationException ex = (UserValidationException) throwable;
                    List<String> errors = ex.getErrors();
                    assert errors.contains("First name is required");
                    assert errors.contains("Last name is required");
                    assert errors.contains("Email is required");
                    assert errors.contains("Document ID is required");
                    assert errors.contains("Birth date is required");
                    assert errors.contains("Address is required");
                    assert errors.contains("Base salary is required");
                })
                .verify();
    }

    @Test
    void validateUser_whenEmailAndSalaryInvalid_shouldThrowSpecificErrors() {
        // Arrange
        User invalidUser = new User();
        invalidUser.setFirstname("Alice");
        invalidUser.setLastname("Smith");
        invalidUser.setEmail("invalid-email"); // Formato incorrecto
        invalidUser.setDocumentId("987654321");
        invalidUser.setBirthdate(LocalDate.of(1985, 5, 5));
        invalidUser.setAddress("456 Elm St");
        invalidUser.setBaseSalary(new BigDecimal("20000000")); // Mayor a 15,000,000

        // Act
        Mono<User> result = UserDomainValidation.validateUser(invalidUser);

        // Assert
        StepVerifier.create(result)
                .expectErrorSatisfies(throwable -> {
                    assert throwable instanceof UserValidationException;
                    UserValidationException ex = (UserValidationException) throwable;
                    List<String> errors = ex.getErrors();
                    assert errors.contains("Email format is invalid");
                    assert errors.contains("Base salary cannot exceed 15,000,000");
                })
                .verify();
    }
}