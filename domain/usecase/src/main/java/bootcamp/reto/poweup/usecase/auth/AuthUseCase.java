package bootcamp.reto.poweup.usecase.auth;

import bootcamp.reto.poweup.model.auth.AuthRequest;
import bootcamp.reto.poweup.model.auth.AuthResponse;
import bootcamp.reto.poweup.model.auth.gateways.JwtTokenRepository;
import bootcamp.reto.poweup.model.user.gateways.UserRepository;
import bootcamp.reto.poweup.usecase.ConstantsUUC;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class AuthUseCase {
    private final JwtTokenRepository jwtTokenRepository;
    private final UserRepository userRepository;

    public Mono<AuthResponse> login(AuthRequest authRequest) {
        return userRepository.authenticateUser(authRequest.getEmail(), authRequest.getPassword())
                .flatMap(user -> jwtTokenRepository.generateToken(user.getEmail(), user.getRole_id())
                        .map(token -> AuthResponse.builder()
                                .token(token)
                                .tokenType("Bearer")
                                .email(user.getEmail())
                                .expiresIn(ConstantsUUC.EXPIRATION_TIME_TOKEN)
                                .build()
                        ));
    }
    public Mono<Boolean> validateToken(String token) {
        return jwtTokenRepository.validateToken(token);
    }
}