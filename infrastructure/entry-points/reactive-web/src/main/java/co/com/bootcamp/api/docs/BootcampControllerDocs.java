package co.com.bootcamp.api.docs;

import co.com.bootcamp.api.BootcampHandler;
import co.com.bootcamp.api.dto.BootcampPageResponseDto;
import co.com.bootcamp.api.dto.BootcampRequestDto;
import co.com.bootcamp.api.dto.BootcampResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
            ),
            @RouterOperation(
                    path = "/api/bootcamp",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.GET,
                    beanClass = BootcampHandler.class,
                    beanMethod = "listenListarBootcamps",
                    operation = @Operation(
                            operationId = "listarBootcamps",
                            summary = "Listar bootcamps",
                            description = "Lista los bootcamps con paginación y ordenamiento. Permite ordenar por nombre, fechaLanzamiento, duracion o cantidadCapacidades. Cada bootcamp incluye sus capacidades asociadas con sus tecnologías.",
                            parameters = {
                                    @Parameter(
                                            name = "page",
                                            description = "Número de página (comienza en 0)",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "integer", defaultValue = "0", example = "0")
                                    ),
                                    @Parameter(
                                            name = "size",
                                            description = "Tamaño de página (número de elementos por página)",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "integer", defaultValue = "10", example = "10")
                                    ),
                                    @Parameter(
                                            name = "sortBy",
                                            description = "Campo por el cual ordenar: 'nombre', 'fechaLanzamiento', 'duracion' o 'cantidadCapacidades'",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "string", defaultValue = "nombre", example = "nombre")
                                    ),
                                    @Parameter(
                                            name = "sortDirection",
                                            description = "Dirección del ordenamiento: 'ASC' o 'DESC'",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "string", defaultValue = "ASC", example = "ASC")
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Lista de bootcamps obtenida exitosamente",
                                            content = @Content(schema = @Schema(implementation = BootcampPageResponseDto.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Error de validación en los parámetros"
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error interno del servidor"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/bootcamp/{id}",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.DELETE,
                    beanClass = BootcampHandler.class,
                    beanMethod = "listenEliminarBootcamp",
                    operation = @Operation(
                            operationId = "eliminarBootcamp",
                            summary = "Eliminar bootcamp",
                            description = "Elimina un bootcamp del sistema utilizando el patrón Saga. El proceso desactiva primero las tecnologías asociadas a las capacidades del bootcamp, luego desactiva las capacidades, y finalmente elimina el bootcamp. Si ocurre un error en cualquier etapa, se realiza rollback reactivando los recursos desactivados.",
                            parameters = {
                                    @Parameter(
                                            name = "id",
                                            description = "ID del bootcamp a eliminar",
                                            in = ParameterIn.PATH,
                                            required = true,
                                            schema = @Schema(type = "string", example = "1")
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "204",
                                            description = "Bootcamp eliminado exitosamente"
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Bootcamp no encontrado"
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error interno del servidor. Si ocurre un error durante la saga, se realiza rollback automático."
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(BootcampHandler handler);
}

