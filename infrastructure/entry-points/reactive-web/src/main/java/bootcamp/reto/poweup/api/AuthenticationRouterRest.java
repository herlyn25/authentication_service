package bootcamp.reto.poweup.api;

import bootcamp.reto.poweup.api.dto.UserDTO;
import bootcamp.reto.poweup.model.auth.AuthRequest;
import bootcamp.reto.poweup.model.user.UserClient;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.parameters.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.*;
import org.springframework.context.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.server.*;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Tag(name="Users",description = "Operations about users")
public class AuthenticationRouterRest {

    @Bean
    @RouterOperations(value = {
            @RouterOperation(
                    path = "/api/v1/users",
                    method = RequestMethod.POST,
                    consumes = {MediaType.APPLICATION_JSON_VALUE},
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    beanClass = AuthenticationHandler.class,
                    beanMethod = "listenSaveUser",
                    operation = @Operation(
                            operationId = "saveUser",
                            summary = "Crear usuario",
                            tags = {"Users"},
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Datos de la solicitud de credito a crear",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = UserDTO.class // ← cambia al paquete real de tu DTO si es distinto
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
            @RouterOperation(
                    path = "/api/v1/login",
                    method = RequestMethod.POST,
                    consumes = {MediaType.APPLICATION_JSON_VALUE},
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    beanClass = AuthenticationHandler.class,
                    beanMethod = "listenUserLogin",
                    operation = @Operation(
                            operationId = "login",
                            summary = "Hacer Login",
                            tags = {"Users"},
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Obtener un tokens para login",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = AuthRequest.class // ← cambia al paquete correcto
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Ok",
                                            content = @Content(
                                                    schema = @Schema(implementation = bootcamp.reto.poweup.model.auth.AuthRequest.class)
                                            )
                                    ),
                                    @ApiResponse(responseCode = "401", description = "No Authorized"),
                                    @ApiResponse(responseCode = "403", description = "Forbbiden"),

                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/users/{param}",
                    method = RequestMethod.GET,
                    consumes = {MediaType.APPLICATION_JSON_VALUE},
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    beanClass = AuthenticationHandler.class,
                    beanMethod = "listenUserByEmailOrDocument",
                    operation = @Operation(
                            operationId = "users_by_email_or_document_id",
                            summary = "Obtener Salario base y nombre del cliente",
                            tags = {"Users"},
                            parameters = {
                                @Parameter(
                                        name="param",
                                        description = "Puede ser el email or documentId",
                                        example = "h@h.com or 10209393",
                                        schema = @Schema(type = "String", defaultValue = "")
                                )
                            }
                            ,responses = {
                                    @ApiResponse(responseCode = "200", description = "Ok",
                                            content = @Content(
                                                    schema = @Schema(implementation = bootcamp.reto.poweup.model.user.UserClient.class)
                                            )
                                    ),
                                    @ApiResponse(responseCode = "200", description = "OK"),
                                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                                    @ApiResponse(responseCode = "404", description = "Bad Request"),
                                    @ApiResponse(responseCode = "403", description = "Forbidden"),

                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> userRouterFunction(AuthenticationHandler authenticationHandler) {
        return route(POST("/api/v1/users"), authenticationHandler::listenSaveUser)
               .andRoute(POST("/api/v1/login"),authenticationHandler::listenUserLogin)
                .andRoute(GET("/api/v1/users/{param}"),authenticationHandler::listenUserByEmailOrDocument);
    }
}