package co.com.bootcamp.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "DTO de respuesta con los datos de un bootcamp y sus capacidades")
public record BootcampListResponseDto(
        @Schema(description = "Identificador único del bootcamp", example = "1")
        Long id,

        @Schema(description = "Nombre del bootcamp", example = "Bootcamp Full Stack Java")
        String nombre,

        @Schema(description = "Descripción del bootcamp", example = "Programa intensivo de desarrollo Full Stack con Java y React")
        String descripcion,

        @Schema(description = "Fecha de lanzamiento del bootcamp (YYYY-MM-DD)", example = "2025-01-15")
        LocalDate fechaLanzamiento,

        @Schema(description = "Duración del bootcamp en semanas", example = "12")
        Integer duracion,

        @Schema(description = "Lista de capacidades asociadas con id, nombre y tecnologías")
        List<CapacidadInfoDto> capacidades
) {
}

