package bootcamp.reto.poweup.api.dto;


import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserDTO(
        @NotBlank(message = "firstname is required")
        String firstname,

        @NotBlank(message = "lastname is required")
        String lastname,

        @Past(message = "Birthdate must be in the past")
        LocalDate birthdate,

        String address,

        Integer phone,

        @NotBlank(message = "email is required")
        @Email(message = "Email format is invalid")
        String email,

        String documentId,

        @NotBlank(message = "Base Salary is required")
        @Min(message = "Base Salary must be at least 0", value = 0)
        @Max(message = "Base Salary must be a maximum of 15000000", value = 15000000)
        BigDecimal baseSalary,

        @NotBlank(message = "Password is required")
        String password
        /*@JsonAlias({"roleId","rolId"})
        Long roleId*/
) {
    public UserDTO{
        if(address==null || address.isEmpty()) address="no address";
        if(phone==null ) phone=0;
        if(birthdate==null) birthdate=null;
    }
}
