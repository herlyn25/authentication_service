package bootcamp.reto.poweup.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder @AllArgsConstructor @NoArgsConstructor
@Table("role")
public class RoleEntity {
    @Id
    private Long id;
    private String code;
    private String name;
    private String description;
}
