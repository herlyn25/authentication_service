package bootcamp.reto.poweup.model.user;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    void user_valid() {
        User user = new User("Erlin",
                "Castillo",
                LocalDate.parse("1988-07-25"),
                "Carrera 5",
                350988556,
                "herly-1988@hotmail.com",
                "1050",
                BigDecimal.valueOf(200000));
        assertEquals("Erlin", user.getFirstname());
        assertEquals("Castillo", user.getLastname());
        assertEquals(LocalDate.parse("1988-07-25"), user.getBirthdate());
        assertEquals("Carrera 5", user.getAddress());
        assertEquals(350988556, user.getPhone());
        assertEquals("herly-1988@hotmail.com", user.getEmail());
        assertEquals("1050", user.getDocumentId());
        assertEquals(BigDecimal.valueOf(200000), user.getBaseSalary());

    }
}
