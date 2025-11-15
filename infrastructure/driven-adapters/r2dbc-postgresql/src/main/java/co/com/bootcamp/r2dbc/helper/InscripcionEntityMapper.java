package co.com.bootcamp.r2dbc.helper;

import co.com.bootcamp.model.bootcamp.inscripcion.Inscripcion;
import co.com.bootcamp.r2dbc.entities.InscripcionEntity;
import org.springframework.stereotype.Component;

@Component
public class InscripcionEntityMapper {

    public InscripcionEntity toEntity(Inscripcion inscripcion) {
        if (inscripcion == null) {
            return null;
        }

        return InscripcionEntity.builder()
                .id(inscripcion.getId())
                .personaId(inscripcion.getPersonaId())
                .bootcampId(inscripcion.getBootcampId())
                .fechaInscripcion(inscripcion.getFechaInscripcion())
                .build();
    }

    public Inscripcion toDomain(InscripcionEntity entity) {
        if (entity == null) {
            return null;
        }

        return Inscripcion.builder()
                .id(entity.getId())
                .personaId(entity.getPersonaId())
                .bootcampId(entity.getBootcampId())
                .fechaInscripcion(entity.getFechaInscripcion())
                .build();
    }
}

