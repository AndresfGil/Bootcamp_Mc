package co.com.bootcamp.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "DTO para la respuesta de una inscripción")
public record InscripcionResponseDto(
        @Schema(description = "ID único de la inscripción generado por el sistema", example = "1")
        Long id,

        @Schema(description = "ID de la persona inscrita", example = "1")
        Long personaId,

        @Schema(description = "ID del bootcamp", example = "1")
        Long bootcampId,

        @Schema(description = "Fecha de inscripción", example = "2025-01-15T10:30:00")
        LocalDateTime fechaInscripcion
) {
}

