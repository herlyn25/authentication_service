package bootcamp.reto.poweup.model.user.gateways;

import bootcamp.reto.poweup.model.user.auth.JwtTokenInfo;
import reactor.core.publisher.Mono;

public interface JwtTokenRepository {
    Mono<String> generateToken(String email);
    Mono<JwtTokenInfo> validateToken(String token);
    Mono<String> extractEmail(String token);
    Mono<Boolean> isTokenExpired(String token);
}
