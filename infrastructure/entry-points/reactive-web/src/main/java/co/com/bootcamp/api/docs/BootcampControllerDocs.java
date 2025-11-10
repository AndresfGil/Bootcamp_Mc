package co.com.bootcamp.api.docs;

import co.com.bootcamp.api.BootcampHandler;
import co.com.bootcamp.api.dto.BootcampRequestDto;
import co.com.bootcamp.api.dto.BootcampResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

public interface BootcampControllerDocs {

    @RouterOperations({
            @RouterOperation(
                    path = "/api/bootcamp",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.POST,
                    beanClass = BootcampHandler.class,
                    beanMethod = "listenGuardarBootcamp",
                    operation = @Operation(
                            operationId = "createBootcamp",
                            summary = "Guardar bootcamp",
                            description = "Crea un nuevo bootcamp en el sistema. Un bootcamp debe tener entre 1 y 4 capacidades asociadas.",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = BootcampRequestDto.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Bootcamp creado exitosamente",
                                            content = @Content(schema = @Schema(implementation = BootcampResponseDto.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Error de validación en los datos enviados o cantidad de capacidades inválida"
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error interno del servidor"
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(BootcampHandler handler);
}

