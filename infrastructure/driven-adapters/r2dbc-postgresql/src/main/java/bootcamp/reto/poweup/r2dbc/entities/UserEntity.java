package bootcamp.reto.poweup.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")

public class UserEntity {

    @Id
    private Long id;
    @Column("firstname") private String firstname;
    @Column("lastname") private String lastname;
    @Column("birthdate") private LocalDate birthdate;
    @Column("address") private String address;
    @Column("phone") private Integer phone;
    @Column("email") private String email;
    @Column("document_id") private String documentId;
    @Column("base_salary") private BigDecimal baseSalary;
    // @Column("id_role") private Long roleId;
}
