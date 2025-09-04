package bootcamp.reto.poweup.model.user.gateways;
import reactor.core.publisher.Mono;

public interface JwtTokenRepository {
    Mono<String> generateToken(String email);
    Mono<Boolean> isTokenExpired(String token);
}
