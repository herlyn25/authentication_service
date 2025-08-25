package bootcamp.reto.poweup.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RoleRouterRest {

    @Bean
    public RouterFunction<ServerResponse> roleRouterFunction(RoleHandler roleHandler) {
        return route(POST("/api/v1/role"), roleHandler::listenSaveRole )
                .andRoute(GET("/api/v1/role/all"), roleHandler::listenFindAllRoles)
                .andRoute(GET("/api/v1/role/{id}"), roleHandler::listenFindRoleById)
                .andRoute(PUT("/api/v1/role/{id}"), roleHandler::listenUpdateRole)
                .andRoute(DELETE("/api/v1/role/{id}"), roleHandler::listenDeleteRoleById);
    }
}