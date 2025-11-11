package co.com.bootcamp.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO de respuesta paginada con lista de bootcamps")
public record BootcampPageResponseDto(
        @Schema(description = "Lista de bootcamps con sus capacidades")
        List<BootcampListResponseDto> data,

        @Schema(description = "Total de registros", example = "50")
        Long totalRows,

        @Schema(description = "Tamaño de página", example = "10")
        Integer pageSize,

        @Schema(description = "Número de página actual", example = "0")
        Integer pageNum,

        @Schema(description = "Indica si hay más páginas", example = "true")
        Boolean hasNext,

        @Schema(description = "Ordenamiento aplicado", example = "nombre ASC")
        String sort
) {
}

