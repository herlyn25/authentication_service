package bootcamp.reto.poweup.model.user;

import java.math.BigDecimal;
import java.time.LocalDate;

import bootcamp.reto.poweup.model.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String firstname;
    private String lastname;
    private LocalDate birthdate;
    private String address;
    private Integer phone;
    private String email;
    private String documentId;
    private BigDecimal baseSalary;
    //private Role id_rol;

}