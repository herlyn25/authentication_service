package bootcamp.reto.poweup.api;

import bootcamp.reto.poweup.model.user.auth.AuthRequest;
import bootcamp.reto.poweup.api.dto.UserDTO;
import bootcamp.reto.poweup.api.mapper.UserMapper;
import bootcamp.reto.poweup.usecase.auth.AuthUseCase;
import bootcamp.reto.poweup.usecase.role.RoleUseCase;
import bootcamp.reto.poweup.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Slf4j
@Component
@RequiredArgsConstructor
@Validated
public class AuthenticationHandler {
    private final UserUseCase userUseCase;
    private final RoleUseCase roleUseCase;
    private final AuthUseCase authUseCase;
    private final UserMapper userMapper;

    public Mono<ServerResponse> listenSaveUser( ServerRequest serverRequest) {
        log.info("Iniciando el guardado del usuario handler");
        return serverRequest.bodyToMono(UserDTO.class)
                .map(userMapper::dtoToUser)
                .flatMap(userUseCase::save)
                .flatMap(userSaved-> ServerResponse.created(URI.create("/api/v1/users"))
                       .contentType(MediaType.APPLICATION_JSON)
                       .build());
    }

    public Mono<ServerResponse> listenUserLogin( ServerRequest serverRequest) {

        return  serverRequest.bodyToMono(AuthRequest.class)
                .flatMap(authUseCase::login)
                .flatMap(tokens-> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(tokens));
    }
/*
    public Mono<ServerResponse> listenSaveRole(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Role.class)
                .flatMap(roleUseCase::saveRole)
                .flatMap(roleSaved -> ServerResponse.created(URI.create("/api/v1/role"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(roleSaved));
    }*/
}