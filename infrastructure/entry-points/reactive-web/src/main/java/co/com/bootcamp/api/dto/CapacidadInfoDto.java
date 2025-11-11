package co.com.bootcamp.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO con información de una capacidad y sus tecnologías")
public record CapacidadInfoDto(
        @Schema(description = "Identificador único de la capacidad", example = "1")
        Long id,

        @Schema(description = "Nombre de la capacidad", example = "Desarrollo Backend Java")
        String nombre,

        @Schema(description = "Lista de tecnologías asociadas con id y nombre")
        List<TecnologiaInfoDto> tecnologias
) {
}

