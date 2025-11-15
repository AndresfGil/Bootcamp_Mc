package co.com.bootcamp.usecase.bootcamp.enrichment;

import co.com.bootcamp.model.bootcamp.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.bootcamp.BootcampConCapacidades;
import co.com.bootcamp.model.bootcamp.gateways.CapacidadGateway;
import co.com.bootcamp.model.bootcamp.gateways.CapacidadInfo;
import co.com.bootcamp.model.bootcamp.page.CustomPage;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class BootcampEnrichmentService {

    private final CapacidadGateway capacidadGateway;

    public Mono<CustomPage<BootcampConCapacidades>> enriquecerBootcampsConCapacidades(
            CustomPage<Bootcamp> pageBootcamps
    ) {
        if (pageBootcamps.getData().isEmpty()) {
            return Mono.just(buildEmptyPage(pageBootcamps));
        }

        List<Long> allCapacidadesIds = extractAllCapacidadesIds(pageBootcamps.getData());

        return capacidadGateway.obtenerCapacidadesPorIds(allCapacidadesIds)
                .collectMap(CapacidadInfo::getId, capacidad -> capacidad)
                .map(capacidadesMap -> buildEnrichedPage(pageBootcamps, capacidadesMap));
    }

    private List<Long> extractAllCapacidadesIds(List<Bootcamp> bootcamps) {
        return bootcamps.stream()
                .flatMap(bootcamp -> bootcamp.getCapacidadesIds() != null
                        ? bootcamp.getCapacidadesIds().stream()
                        : java.util.stream.Stream.empty())
                .distinct()
                .toList();
    }

    private CustomPage<BootcampConCapacidades> buildEmptyPage(CustomPage<Bootcamp> pageBootcamps) {
        return CustomPage.<BootcampConCapacidades>builder()
                .data(new ArrayList<>())
                .totalRows(pageBootcamps.getTotalRows())
                .pageSize(pageBootcamps.getPageSize())
                .pageNum(pageBootcamps.getPageNum())
                .hasNext(pageBootcamps.getHasNext())
                .sort(pageBootcamps.getSort())
                .build();
    }

    private CustomPage<BootcampConCapacidades> buildEnrichedPage(
            CustomPage<Bootcamp> pageBootcamps,
            java.util.Map<Long, CapacidadInfo> capacidadesMap
    ) {
        List<BootcampConCapacidades> bootcampsConCapacidades = pageBootcamps.getData().stream()
                .map(bootcamp -> buildBootcampConCapacidades(bootcamp, capacidadesMap))
                .toList();

        return CustomPage.<BootcampConCapacidades>builder()
                .data(bootcampsConCapacidades)
                .totalRows(pageBootcamps.getTotalRows())
                .pageSize(pageBootcamps.getPageSize())
                .pageNum(pageBootcamps.getPageNum())
                .hasNext(pageBootcamps.getHasNext())
                .sort(pageBootcamps.getSort())
                .build();
    }

    private BootcampConCapacidades buildBootcampConCapacidades(
            Bootcamp bootcamp,
            java.util.Map<Long, CapacidadInfo> capacidadesMap
    ) {
        List<CapacidadInfo> capacidades = bootcamp.getCapacidadesIds() != null
                ? bootcamp.getCapacidadesIds().stream()
                        .map(capacidadesMap::get)
                        .filter(Objects::nonNull)
                        .toList()
                : new ArrayList<>();

        return BootcampConCapacidades.builder()
                .id(bootcamp.getId())
                .nombre(bootcamp.getNombre())
                .descripcion(bootcamp.getDescripcion())
                .fechaLanzamiento(bootcamp.getFechaLanzamiento())
                .duracion(bootcamp.getDuracion())
                .capacidades(capacidades)
                .build();
    }
}

