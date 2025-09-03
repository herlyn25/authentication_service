package bootcamp.reto.poweup.model.user.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenInfo {
    private String email;
    private String token;
    private boolean isValid;
    private boolean isExpired;
}
