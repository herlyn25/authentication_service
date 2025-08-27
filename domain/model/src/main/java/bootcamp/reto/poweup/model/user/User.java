package bootcamp.reto.poweup.model.user;

import java.math.BigDecimal;
import java.time.LocalDate;

import bootcamp.reto.poweup.model.role.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

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

    public User(Long id, String firstname, String lastname, LocalDate birthdate, String address, Integer phone, String email,
            String documentId, BigDecimal baseSalary /*, Role id_rol?*/) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.documentId = documentId;
        this.baseSalary = baseSalary;
        // this.id_rol = id_rol;
    }

}