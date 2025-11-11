package co.com.bootcamp.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO con información básica de una tecnología")
public record TecnologiaInfoDto(
        @Schema(description = "Identificador único de la tecnología", example = "1")
        Long id,

        @Schema(description = "Nombre de la tecnología", example = "Java")
        String nombre
) {
}

