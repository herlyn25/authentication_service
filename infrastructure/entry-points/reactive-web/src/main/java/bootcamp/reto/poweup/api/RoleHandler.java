package bootcamp.reto.poweup.api;

import bootcamp.reto.poweup.model.role.Role;
import bootcamp.reto.poweup.usecase.role.RoleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class RoleHandler {
    private final RoleUseCase roleUseCase;

    public Mono<ServerResponse> listenSaveRole(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Role.class)
                .flatMap(roleUseCase::saveRole)
                .flatMap(roleSaved -> ServerResponse.created(URI.create("/api/v1/role"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(roleSaved));
    }

    public Mono<ServerResponse> listenFindRoleById(ServerRequest serverRequest) {
       Long id = Long.parseLong(serverRequest.pathVariable("id"));
        return roleUseCase.findRoleById(id)
                .flatMap(role-> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(role))
                .switchIfEmpty(
                        ServerResponse.status(HttpStatus.NOT_FOUND)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(Collections.singletonMap("message",String.format("Role with id %s no exists", id)))
                );
    }

    public Mono<ServerResponse> listenFindAllRoles(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(roleUseCase.findAllRoles(), Role.class);
    }

    public Mono<ServerResponse> listenDeleteRoleById(ServerRequest serverRequest) {
        Long id = Long.parseLong(serverRequest.pathVariable("id"));
        Mono<Role> role = roleUseCase.findRoleById(id);

        return role.flatMap( roleDeleted -> {
                            roleUseCase.deleteRoleById(id);
                            return ServerResponse.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(
                                            Collections.singletonMap("message",String.format("Role with id %s has been deleted", id))
                                    );
                        })
                    .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Collections.singletonMap("message", String.format("role with id %s no exists",id))));
    }

    public Mono<ServerResponse> listenUpdateRole(ServerRequest serverRequest) {
        Mono<Role> role = serverRequest.bodyToMono(Role.class);
        Long id = Long.parseLong(serverRequest.pathVariable("id"));
        Mono<Role> roleUpdated = role.flatMap(role1 -> roleUseCase.findRoleById(id)
                .flatMap(roleOld -> {
                    roleOld.setName(role1.getName());
                    roleOld.setDescription(role1.getDescription());
                    return roleUseCase.saveRole(roleOld);
                }));
        return roleUpdated.flatMap(role2 ->
                ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(role2))
        ).switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Collections.singletonMap("message",String.format("Role with id %s was not found",id))));
    }
}