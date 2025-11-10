package co.com.bootcamp.api.helpers;

import co.com.bootcamp.api.dto.BootcampRequestDto;
import co.com.bootcamp.api.dto.BootcampResponseDto;
import co.com.bootcamp.model.bootcamp.Bootcamp;
import org.springframework.stereotype.Component;

@Component
public class BootcampMapper {

    public Bootcamp toDomain(BootcampRequestDto dto) {
        return Bootcamp.builder()
                .nombre(dto.nombre())
                .descripcion(dto.descripcion())
                .fechaLanzamiento(dto.fechaLanzamiento())
                .duracion(dto.duracion())
                .capacidadesIds(dto.capacidadesIds())
                .build();
    }

    public BootcampResponseDto toResponseDto(Bootcamp bootcamp) {
        return new BootcampResponseDto(
                bootcamp.getId(),
                bootcamp.getNombre(),
                bootcamp.getDescripcion(),
                bootcamp.getFechaLanzamiento(),
                bootcamp.getDuracion(),
                bootcamp.getCapacidadesIds()
        );
    }
}
