package bootcamp.reto.poweup.r2dbc.mapper;

import bootcamp.reto.poweup.model.user.User;
import bootcamp.reto.poweup.r2dbc.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User dtoToUser(UserDTO userDTO){
        User user = new User();
        user.setId(null);
        user.setFirstname(userDTO.firstname());
        user.setLastname(userDTO.lastname());
        user.setBirthdate(userDTO.birthdate());
        user.setAddress(userDTO.address());
        user.setPhone(userDTO.phone());
        user.setEmail(userDTO.email());
        user.setDocumentId(userDTO.documentId());
        user.setBaseSalary(userDTO.baseSalary());
        return user;
    }

}
