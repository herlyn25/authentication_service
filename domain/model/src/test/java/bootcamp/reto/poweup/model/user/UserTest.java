package bootcamp.reto.poweup.model.user;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    void user_valid() {
        User user = User.builder()
                .id(1L)
                .firstname("Erlin")
                .lastname("Castillo")
                .birthdate(LocalDate.parse("1988-07-25"))
                .address("Carrera 5")
                .phone(350988556)
                .email("herly-1988@hotmail.com")
                .documentId("1050")
                .baseSalary( new BigDecimal("200000"))
                .build();
        assertNotNull(user);
        assertEquals("Erlin", user.getFirstname());
        assertEquals("Castillo", user.getLastname());
        assertEquals(LocalDate.parse("1988-07-25"), user.getBirthdate());
        assertEquals("Carrera 5", user.getAddress());
        assertEquals(350988556, user.getPhone());
        assertEquals("herly-1988@hotmail.com", user.getEmail());
        assertEquals("1050", user.getDocumentId());
        assertEquals(new BigDecimal("200000"), user.getBaseSalary());
    }
}