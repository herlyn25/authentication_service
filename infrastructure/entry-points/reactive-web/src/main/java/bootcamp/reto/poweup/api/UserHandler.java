package bootcamp.reto.poweup.api;

import bootcamp.reto.poweup.model.user.User;
import bootcamp.reto.poweup.usecase.role.RoleUseCase;
import bootcamp.reto.poweup.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class UserHandler {
    private final UserUseCase userUseCase;
    private final RoleUseCase roleUseCase;

    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(User.class)
               .flatMap(userUseCase::saveUser)
                .flatMap(userSaved-> ServerResponse.created(URI.create("/api/v1/users"))
                       .contentType(MediaType.APPLICATION_JSON)
                       .bodyValue(userSaved));
    }

    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        // useCase2.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }
}
