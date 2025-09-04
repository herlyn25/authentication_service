package bootcamp.reto.poweup.model.auth.gateways;
import reactor.core.publisher.Mono;

public interface JwtTokenRepository {
    Mono<String> generateToken(String email, Long roleId);

}
