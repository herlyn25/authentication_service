package bootcamp.reto.poweup.usecase.auth;

import bootcamp.reto.poweup.model.ConstanstsModel;
import bootcamp.reto.poweup.model.user.User;
import bootcamp.reto.poweup.model.user.auth.AuthRequest;
import bootcamp.reto.poweup.model.user.auth.AuthResponse;
import bootcamp.reto.poweup.model.user.auth.JwtTokenInfo;
import bootcamp.reto.poweup.model.user.exceptions.InvalidCredentials;
import bootcamp.reto.poweup.model.user.gateways.JwtTokenRepository;
import bootcamp.reto.poweup.model.user.gateways.UserRepository;
import bootcamp.reto.poweup.usecase.ConstanstsUUC;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor

public class AuthUseCase {
    private final JwtTokenRepository jwtTokenRepository;
    private final UserRepository userRepository;

    public Mono<AuthResponse> login(AuthRequest authRequest) {
        return userRepository.authenticateUser(authRequest.getEmail(), authRequest.getPassword())
                .flatMap(user -> jwtTokenRepository.generateToken(user.getEmail())
                        .map(token -> AuthResponse.builder()
                                .token(token)
                                .email(user.getEmail())
                                //.role(user.getId_rol().getCode())
                                .expiresIn(ConstanstsUUC.EXPIRES_TIME_TOKEN)
                                .build()
                        ));
    }

    public Mono<JwtTokenInfo> validateToken(String token){
        return jwtTokenRepository.validateToken(token);
    }

    public Mono<User> getCurrentUser(String token) {
        return jwtTokenRepository.extractEmail(token)
                .flatMap(userRepository::findUserByEmail);
    }
}