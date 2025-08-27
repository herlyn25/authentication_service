package bootcamp.reto.poweup.exceptions;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;
import java.util.Map;

@Component
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalExceptionHandler(ErrorAttributes errorAttributes,
                                  ApplicationContext applicationContext,
                                  ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        this.setMessageWriters(serverCodecConfigurer.getWriters());
        this.setMessageReaders(serverCodecConfigurer.getReaders());
}

    @Override
    protected  RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> errorProperties = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        Throwable error = getError(request);
        HttpStatus status = determineHttpStatus(error);

        // Crear respuesta personalizada
        Map<String, Object> errorResponse = Map.of(
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", determineErrorMessage(error),
                "timestamp", errorProperties.getOrDefault("timestamp", java.time.Instant.now().toString()),
                "path", errorProperties.getOrDefault("path", request.path())
        );
        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorResponse));
    }
    private HttpStatus determineHttpStatus(Throwable error) {
        if (error instanceof IllegalArgumentException) {
            return HttpStatus.BAD_REQUEST;
        }
        if (error instanceof RuntimeException) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.BAD_REQUEST;
    }

    private String determineErrorMessage(Throwable error) {
        return error.getMessage() != null ? error.getMessage() : "Error interno del servidor";
    }
}