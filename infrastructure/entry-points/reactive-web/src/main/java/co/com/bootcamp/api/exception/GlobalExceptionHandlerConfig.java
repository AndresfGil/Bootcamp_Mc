package co.com.bootcamp.api.exception;

import co.com.bootcamp.model.bootcamp.exception.BaseException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@Slf4j
public class GlobalExceptionHandlerConfig {

    @Bean
    public DefaultErrorAttributes defaultErrorAttributes() {
        return new DefaultErrorAttributes();
    }

    @Bean
    @Order(-2)
    public ErrorWebExceptionHandler globalExceptionHandler(DefaultErrorAttributes errorAttributes,
                                                           ApplicationContext applicationContext) {
        WebProperties.Resources resources = new WebProperties.Resources();
        return new GlobalErrorWebExceptionHandler(errorAttributes, resources, applicationContext);
    }

    static class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

        GlobalErrorWebExceptionHandler(DefaultErrorAttributes g, WebProperties.Resources r, ApplicationContext c) {
            super(g, r, c);
            super.setMessageReaders(ServerCodecConfigurer.create().getReaders());
            super.setMessageWriters(ServerCodecConfigurer.create().getWriters());
        }

        @Override
        protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
            return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
        }

        private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
            Throwable ex = getError(request);
            String correlationId = request.headers().firstHeader("X-Correlation-Id");

            ErrorResponse payload;
            HttpStatus status;

            log.error("[ERROR] {} on path={} correlationId={}", ex.getClass().getSimpleName(), request.path(), correlationId, ex);
            log.error("Exception details: {}", ex.getMessage());

            switch (ex) {
                case ConstraintViolationException cve -> {
                    status = HttpStatus.BAD_REQUEST;
                    List<ErrorDetail> details = cve.getConstraintViolations().stream()
                            .map(v -> new ErrorDetail(v.getPropertyPath().toString(), v.getMessage()))
                            .toList();
                    payload = ErrorResponse.of(
                            "VALIDATION_ERROR",
                            "Datos de entrada inválidos",
                            status.value(),
                            request.path(),
                            details,
                            correlationId
                    );
                }
                case BaseException be -> {
                    status = HttpStatus.valueOf(be.getStatusCode());
                    List<ErrorDetail> details = be.getErrors() == null ? null : be.getErrors().stream()
                            .map(msg -> {
                                if (msg != null && msg.contains(" ")) {
                                    int firstSpaceIndex = msg.indexOf(" ");
                                    String field = msg.substring(0, firstSpaceIndex);
                                    String message = msg.substring(firstSpaceIndex + 1);
                                    return new ErrorDetail(field, message);
                                }
                                String message = msg != null ? msg : "";
                                return new ErrorDetail("", message);
                            })
                            .toList();
                    payload = ErrorResponse.of(
                            be.getErrorCode(),
                            be.getMessage(),
                            status.value(),
                            request.path(),
                            details,
                            correlationId
                    );
                }
                case IllegalArgumentException iae -> {
                    status = HttpStatus.BAD_REQUEST;
                    payload = ErrorResponse.of(
                            "INVALID_ARGUMENT",
                            iae.getMessage(),
                            status.value(),
                            request.path(),
                            null,
                            correlationId
                    );
                }
                default -> {
                    status = HttpStatus.INTERNAL_SERVER_ERROR;
                    payload = ErrorResponse.of(
                            "INTERNAL_ERROR",
                            "Ocurrió un error inesperado",
                            status.value(),
                            request.path(),
                            null,
                            correlationId
                    );
                }
            }

            return ServerResponse.status(status)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(payload));
        }
    }
}
