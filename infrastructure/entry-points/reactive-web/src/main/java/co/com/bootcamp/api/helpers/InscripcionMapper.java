package co.com.bootcamp.api.helpers;

import co.com.bootcamp.api.dto.InscripcionRequestDto;
import co.com.bootcamp.api.dto.InscripcionResponseDto;
import co.com.bootcamp.model.bootcamp.inscripcion.Inscripcion;
import org.springframework.stereotype.Component;

@Component
public class InscripcionMapper {

    public Inscripcion toDomain(InscripcionRequestDto dto) {
        return Inscripcion.builder()
                .personaId(dto.personaId())
                .bootcampId(dto.bootcampId())
                .build();
    }

    public InscripcionResponseDto toResponseDto(Inscripcion inscripcion) {
        return new InscripcionResponseDto(
                inscripcion.getId(),
                inscripcion.getPersonaId(),
                inscripcion.getBootcampId(),
                inscripcion.getFechaInscripcion()
        );
    }
}
