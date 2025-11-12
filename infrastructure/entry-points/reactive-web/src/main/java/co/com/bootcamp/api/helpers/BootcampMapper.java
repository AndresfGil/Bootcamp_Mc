package co.com.bootcamp.api.helpers;

import co.com.bootcamp.api.dto.BootcampListResponseDto;
import co.com.bootcamp.api.dto.BootcampPageResponseDto;
import co.com.bootcamp.api.dto.BootcampRequestDto;
import co.com.bootcamp.api.dto.BootcampResponseDto;
import co.com.bootcamp.api.dto.CapacidadInfoDto;
import co.com.bootcamp.api.dto.TecnologiaInfoDto;
import co.com.bootcamp.model.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.BootcampConCapacidades;
import co.com.bootcamp.model.bootcamp.gateways.CapacidadInfo;
import co.com.bootcamp.model.bootcamp.gateways.TecnologiaInfo;
import co.com.bootcamp.model.bootcamp.page.CustomPage;
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

    public BootcampPageResponseDto toPageResponseDto(CustomPage<BootcampConCapacidades> page) {
        return new BootcampPageResponseDto(
                page.getData().stream()
                        .map(this::toListResponseDto)
                        .toList(),
                page.getTotalRows(),
                page.getPageSize(),
                page.getPageNum(),
                page.getHasNext(),
                page.getSort()
        );
    }

    private BootcampListResponseDto toListResponseDto(BootcampConCapacidades bootcamp) {
        return new BootcampListResponseDto(
                bootcamp.getId(),
                bootcamp.getNombre(),
                bootcamp.getDescripcion(),
                bootcamp.getFechaLanzamiento(),
                bootcamp.getDuracion(),
                bootcamp.getCapacidades().stream()
                        .map(this::toCapacidadInfoDto)
                        .toList()
        );
    }

    private CapacidadInfoDto toCapacidadInfoDto(CapacidadInfo capacidad) {
        return new CapacidadInfoDto(
                capacidad.getId(),
                capacidad.getNombre(),
                capacidad.getTecnologias() != null
                        ? capacidad.getTecnologias().stream()
                                .map(this::toTecnologiaInfoDto)
                                .toList()
                        : java.util.Collections.emptyList()
        );
    }

    private TecnologiaInfoDto toTecnologiaInfoDto(TecnologiaInfo tecnologia) {
        return new TecnologiaInfoDto(
                tecnologia.getId(),
                tecnologia.getNombre()
        );
    }
}
