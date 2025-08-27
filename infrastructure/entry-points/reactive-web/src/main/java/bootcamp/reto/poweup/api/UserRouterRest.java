package bootcamp.reto.poweup.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouterRest {

    @Bean
    public RouterFunction<ServerResponse> userRouterFunction(UserHandler userHandler) {
        return route(POST("/api/v1/users"), userHandler::listenSaveUser)
                .andRoute(GET("/api/v1/users"), userHandler::listenFindAllUsers)
                .and(route(GET("/api/v1/user/{id}"), userHandler::listenFindByDocumentId));
    }
}
