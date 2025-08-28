package bootcamp.reto.poweup.api;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.parameters.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.*;
import org.springframework.context.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.*;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Tag(name="Users",description = "Operations about users")
public class AuthenticationRouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/users",
                    method = RequestMethod.POST,
                    consumes = { MediaType.APPLICATION_JSON_VALUE },
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    beanClass = AuthenticationHandler.class,
                    beanMethod = "listenSaveUser",
                    operation = @Operation(
                            operationId = "saveUser",
                            summary = "Crear usuario",
                            tags = { "Users" },
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Datos del usuario a crear",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = bootcamp.reto.poweup.r2dbc.dto.UserDTO.class // ← cambia al paquete real de tu DTO si es distinto
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Creado",
                                            content = @Content(
                                                    schema = @Schema(implementation = bootcamp.reto.poweup.model.user.User.class)
                                            )
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Solicitud inválida")
                            }
                    )
            )})
    public RouterFunction<ServerResponse> userRouterFunction(AuthenticationHandler authenticationHandler) {
        return route(POST("/api/v1/users"), authenticationHandler::listenSaveUser);
    }

    @Bean
    public RouterFunction<ServerResponse> roleRouterFunction(AuthenticationHandler roleHandler) {
        return route(POST("/api/v1/role"), roleHandler::listenSaveRole);
    }
}
