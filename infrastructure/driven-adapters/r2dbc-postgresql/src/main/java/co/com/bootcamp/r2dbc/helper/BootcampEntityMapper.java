package co.com.bootcamp.r2dbc.helper;

import co.com.bootcamp.model.bootcamp.Bootcamp;
import co.com.bootcamp.r2dbc.entities.BootcampEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class BootcampEntityMapper {

    private final com.fasterxml.jackson.databind.ObjectMapper jsonMapper;

    public BootcampEntityMapper(com.fasterxml.jackson.databind.ObjectMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    public BootcampEntity toEntity(Bootcamp bootcamp) {
        if (bootcamp == null) {
            return null;
        }

        BootcampEntity entity = new BootcampEntity();
        entity.setId(bootcamp.getId());
        entity.setNombre(bootcamp.getNombre());
        entity.setDescripcion(bootcamp.getDescripcion());
        entity.setFechaLanzamiento(bootcamp.getFechaLanzamiento());
        entity.setDuracion(bootcamp.getDuracion());

        try {
            String capacidadesJson = jsonMapper.writeValueAsString(
                    bootcamp.getCapacidadesIds() != null ? bootcamp.getCapacidadesIds() : Collections.emptyList()
            );
            entity.setCapacidadesIds(capacidadesJson);
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir capacidadesIds a JSON", e);
        }

        return entity;
    }

    public Bootcamp toDomain(BootcampEntity entity) {
        if (entity == null) {
            return null;
        }

        Bootcamp bootcamp = new Bootcamp();
        bootcamp.setId(entity.getId());
        bootcamp.setNombre(entity.getNombre());
        bootcamp.setDescripcion(entity.getDescripcion());
        bootcamp.setFechaLanzamiento(entity.getFechaLanzamiento());
        bootcamp.setDuracion(entity.getDuracion());

        try {
            List<Long> capacidadesIds = entity.getCapacidadesIds() != null && !entity.getCapacidadesIds().isEmpty()
                    ? jsonMapper.readValue(entity.getCapacidadesIds(), new TypeReference<List<Long>>() {})
                    : Collections.emptyList();
            bootcamp.setCapacidadesIds(capacidadesIds);
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir JSON a capacidadesIds", e);
        }

        return bootcamp;
    }
}
