package bootcamp.reto.poweup.api;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Tag(name="Users",description = "Operations about users")
public class UserRouterRest {

    @Bean
    @RouterOperations({
            // === POST /api/v1/users  (crear) ===
            @RouterOperation(
                    path = "/api/v1/users",
                    method = RequestMethod.POST,
                    consumes = { MediaType.APPLICATION_JSON_VALUE },
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    beanClass = UserHandler.class,
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
            ),

            // === GET /api/v1/users  (listar) ===
            @RouterOperation(
                    path = "/api/v1/users",
                    method = RequestMethod.GET,
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    beanClass = UserHandler.class,
                    beanMethod = "listenFindAllUsers",
                    operation = @Operation(
                            operationId = "findAllUsers",
                            summary = "Listar usuarios",
                            tags = { "Users" },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "OK",
                                            content = @Content(
                                                    array = @ArraySchema(
                                                            schema = @Schema(implementation = bootcamp.reto.poweup.model.user.User.class)
                                                    )
                                            )
                                    )
                            }
                    )
            ),

            // === GET /api/v1/users/{email}  (buscar por email) ===
            @RouterOperation(
                    path = "/api/v1/users/{email}",
                    method = RequestMethod.GET,
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    beanClass = UserHandler.class,
                    beanMethod = "listenFindByEmail",
                    operation = @Operation(
                            operationId = "findUserByEmail",
                            summary = "Buscar usuario por email",
                            tags = { "Users" },
                            parameters = {
                                    @Parameter(
                                            name = "email",
                                            in = ParameterIn.PATH,
                                            required = true,
                                            description = "Correo electrónico del usuario",
                                            schema = @Schema(type = "string"),
                                            example = "john.doe@example.com"
                                    )
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "OK",
                                            content = @Content(
                                                    schema = @Schema(implementation = bootcamp.reto.poweup.model.user.User.class)
                                            )
                                    ),
                                    @ApiResponse(responseCode = "404", description = "No encontrado")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/user/dni/{documentId}",              // Debe coincidir EXACTO con tu route
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.GET,
                    beanClass = UserHandler.class,
                    beanMethod = "listenFindByDocumentId",
                    operation = @Operation(
                            operationId = "findUserByDni",
                            summary = "Buscar usuario por documento",
                            tags = {"Users"},
                            parameters = {
                                    @Parameter(
                                            name = "documentId",
                                            in = ParameterIn.PATH,
                                            required = true,
                                            description = "Documento de identidad (DNI)",
                                            schema = @Schema(type = "string"),
                                            example = "1050978985"
                                    )
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "OK",
                                            content = @Content(schema = @Schema(implementation = bootcamp.reto.poweup.model.user.User.class))),
                                    @ApiResponse(responseCode = "404", description = "No encontrado")
                            }
                    )
            )

    })
    public RouterFunction<ServerResponse> userRouterFunction(UserHandler userHandler) {
        return route(POST("/api/v1/users"), userHandler::listenSaveUser)
                .andRoute(GET("/api/v1/users"), userHandler::listenFindAllUsers)
                .and(route(GET("/api/v1/users/dni/{documentId}"), userHandler::listenFindByDocumentId))
                .andRoute(GET("/api/v1/users/{email}"), userHandler::listenFindByEmail);
    }
}
