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


    public User(String firstname, String lastname, LocalDate birthdate, String address, Integer phone, String email,
                String documentId, BigDecimal baseSalary /*, Role id_rol?*/) {

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