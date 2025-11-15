package co.com.bootcamp.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "DTO para registrar una nueva inscripción")
public record InscripcionRequestDto(
        @Schema(description = "ID de la persona que se inscribe", example = "1", required = true)
        @NotNull(message = "personaId es obligatorio")
        @Positive(message = "personaId debe ser un número positivo")
        Long personaId,

        @Schema(description = "ID del bootcamp al que se inscribe", example = "1", required = true)
        @NotNull(message = "bootcampId es obligatorio")
        @Positive(message = "bootcampId debe ser un número positivo")
        Long bootcampId
) {
}
