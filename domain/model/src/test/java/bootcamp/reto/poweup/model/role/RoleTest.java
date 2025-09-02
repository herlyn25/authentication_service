package bootcamp.reto.poweup.model.role;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoleTest {
    @Test
    void user_valid() {
       Role role = new Role("1N", "Asesor", "asesora las personas");
       assertEquals("Asesor", role.getName());
       assertEquals("asesora las personas", role.getDescription());
       assertEquals("1N", role.getCode());
    }
}
