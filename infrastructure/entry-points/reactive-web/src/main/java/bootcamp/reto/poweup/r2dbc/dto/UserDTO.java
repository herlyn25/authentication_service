package bootcamp.reto.poweup.r2dbc.dto;


import java.math.BigDecimal;
import java.time.LocalDate;

public record UserDTO(
        String firstname,
        String lastname,
        LocalDate birthdate,
        String address,
        Integer phone,
        String email,
        Integer documentId,
        BigDecimal baseSalary
        /*@JsonAlias({"roleId","rolId"})
        Long roleId*/
) {}
