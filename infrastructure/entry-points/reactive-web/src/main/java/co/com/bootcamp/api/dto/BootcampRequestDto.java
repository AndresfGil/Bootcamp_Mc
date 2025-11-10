package co.com.bootcamp.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "DTO para crear un nuevo bootcamp")
public record BootcampRequestDto(
        @Schema(description = "Nombre del bootcamp", example = "Bootcamp Full Stack Java", required = true, maxLength = 100)
        @NotNull(message = "El nombre es obligatorio")
        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
        String nombre,

        @Schema(description = "Descripción del bootcamp", example = "Programa intensivo de desarrollo Full Stack con Java y React", required = true, maxLength = 500)
        @NotNull(message = "La descripción es obligatoria")
        @NotBlank(message = "La descripción no puede estar vacía")
        @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
        String descripcion,

        @Schema(description = "Fecha de lanzamiento del bootcamp", example = "2025-01-15", required = true)
        @NotNull(message = "La fecha de lanzamiento es obligatoria")
        LocalDate fechaLanzamiento,

        @Schema(description = "Duración del bootcamp en semanas", example = "12", required = true)
        @NotNull(message = "La duración es obligatoria")
        @Min(value = 1, message = "La duración debe ser al menos 1 semana")
        @Max(value = 52, message = "La duración no puede exceder 52 semanas")
        Integer duracion,

        @Schema(description = "Lista de IDs de capacidades asociadas (mínimo 1, máximo 4)", example = "[1, 2, 3]", required = true)
        @NotNull(message = "La lista de capacidades es obligatoria")
        @NotEmpty(message = "Debe tener al menos 1 capacidad asociada")
        @Size(min = 1, max = 4, message = "Debe tener entre 1 y 4 capacidades asociadas")
        List<Long> capacidadesIds
) {
}
