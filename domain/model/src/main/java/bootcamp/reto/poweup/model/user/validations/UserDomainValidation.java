package bootcamp.reto.poweup.model.user.validations;

import bootcamp.reto.poweup.model.user.User;
import bootcamp.reto.poweup.model.user.exceptions.UserValidationException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

public class UserDomainValidation {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{7,15}$");

    public static Mono<User> validateUser(User user) {
        List<String> errors = new ArrayList<>();

        // Validar campos obligatorios
        if (user.getFirstname() == null || user.getFirstname().trim().isEmpty()) {
            errors.add("First name is required");
        }

        if (user.getLastname() == null || user.getLastname().trim().isEmpty()) {
            errors.add("Last name is required");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            errors.add("Email is required");
        } else if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            errors.add("Email format is invalid");
        }

        if (user.getDocumentId() == null) {
            errors.add("Document ID is required");
        }

        /*if (user.getPhone() == null) {
            errors.add("Phone is required");
        } else if (!PHONE_PATTERN.matcher(user.getPhone().toString()).matches()) {
            errors.add("Phone format is invalid only numbers or length between 7 and 15 digits");
        }*/

        if (user.getBirthdate() == null) {
            errors.add("Birth date is required");
        }

        if (user.getAddress() == null || user.getAddress().trim().isEmpty()) {
            errors.add("Address is required");
        }

        if (user.getBaseSalary() == null) {
            errors.add("Base salary is required");
        } else if (user.getBaseSalary().compareTo(BigDecimal.ZERO) < 0) {
            errors.add("Base salary cannot be negative");
        } else if (user.getBaseSalary().compareTo(new BigDecimal("15000000")) > 0) {
            errors.add("Base salary cannot exceed 15,000,000");
        }

        /*/ Validaciones de negocio
        if (user.getBirthdate() != null &&
                java.time.Period.between(user.getBirthdate(), java.time.LocalDate.now()).getYears() < 18) {
            errors.add("User must be at least 18 years old");
        }*/

        if (!errors.isEmpty()) {
            return Mono.error(new UserValidationException(errors));
        }
        return Mono.just(user);
    }
}
