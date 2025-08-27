package bootcamp.reto.poweup.api;

import bootcamp.reto.poweup.model.user.User;
import bootcamp.reto.poweup.r2dbc.dto.UserDTO;
import bootcamp.reto.poweup.r2dbc.mapper.UserMapper;
import bootcamp.reto.poweup.usecase.role.RoleUseCase;
import bootcamp.reto.poweup.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
@Validated
public class UserHandler {
    private final UserUseCase userUseCase;
    private final RoleUseCase roleUseCase;
    private final UserMapper userMapper;

    public Mono<ServerResponse> listenSaveUser( ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UserDTO.class)
                .map(userDTO-> {
                    userDTO = new UserDTO(
                            userDTO.firstname(),
                            userDTO.lastname(),
                            userDTO.birthdate(),
                            userDTO.address(),
                            userDTO.phone(),
                            userDTO.email(),
                            userDTO.documentId(),
                            userDTO.baseSalary()
                    );
                    return userMapper.dtoToUser(userDTO);
                }).flatMap(userUseCase::save)
                .flatMap(userSaved-> ServerResponse.created(URI.create("/api/v1/users"))
                       .contentType(MediaType.APPLICATION_JSON)
                       .bodyValue(userSaved));
    }

    public Mono<ServerResponse> listenFindAllUsers(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userUseCase.findUserAll(), User.class);
    }

    public Mono<ServerResponse> listenFindByDocumentId(ServerRequest serverRequest) {
        String documentId =serverRequest.pathVariable("id");
         return userUseCase.findDocumentId(documentId)
                 .flatMap(user -> ServerResponse.ok()
                         .contentType(MediaType.APPLICATION_JSON)
                         .body(fromValue(user)))
                 .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND)
                         .contentType(MediaType.APPLICATION_JSON)
                         .bodyValue(String.format("Document Id %s no exists",documentId)));

    }
}
