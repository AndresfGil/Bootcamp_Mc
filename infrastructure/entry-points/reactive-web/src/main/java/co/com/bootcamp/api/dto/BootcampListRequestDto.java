package co.com.bootcamp.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Schema(description = "DTO para consultar bootcamps con paginación y ordenamiento")
public record BootcampListRequestDto(
        @Schema(description = "Número de página (comienza en 0)", example = "0", defaultValue = "0")
        @Min(value = 0, message = "El número de página debe ser mayor o igual a 0")
        Integer page,

        @Schema(description = "Tamaño de página (número de elementos por página)", example = "10", defaultValue = "10")
        @Min(value = 1, message = "El tamaño de página debe ser mayor a 0")
        @Max(value = 100, message = "El tamaño de página no puede exceder 100")
        Integer size,

        @Schema(description = "Campo por el cual ordenar: 'nombre', 'fechaLanzamiento', 'duracion' o 'cantidadCapacidades'",
                example = "nombre",
                allowableValues = {"nombre", "fechaLanzamiento", "duracion", "cantidadCapacidades"},
                defaultValue = "nombre")
        String sortBy,

        @Schema(description = "Dirección del ordenamiento: 'ASC' o 'DESC'",
                example = "ASC",
                allowableValues = {"ASC", "DESC"},
                defaultValue = "ASC")
        String sortDirection
) {
    public BootcampListRequestDto {
        if (page == null) page = 0;
        if (size == null) size = 10;
        if (sortBy == null || sortBy.isBlank()) sortBy = "nombre";
        if (sortDirection == null || sortDirection.isBlank()) sortDirection = "ASC";
    }
}

