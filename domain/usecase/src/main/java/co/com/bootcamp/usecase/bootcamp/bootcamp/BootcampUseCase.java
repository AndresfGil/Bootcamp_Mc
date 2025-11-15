package co.com.bootcamp.usecase.bootcamp.bootcamp;

import co.com.bootcamp.model.bootcamp.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.bootcamp.BootcampConCapacidades;
import co.com.bootcamp.model.bootcamp.gateways.BootcampRepository;
import co.com.bootcamp.model.bootcamp.gateways.CapacidadGateway;
import co.com.bootcamp.model.bootcamp.gateways.CapacidadInfo;
import co.com.bootcamp.model.bootcamp.page.CustomPage;
import co.com.bootcamp.model.bootcamp.validator.BootcampValidator;
import co.com.bootcamp.usecase.bootcamp.enrichment.BootcampEnrichmentService;
import co.com.bootcamp.usecase.bootcamp.saga.BootcampDeletionSagaOrchestrator;
import co.com.bootcamp.usecase.bootcamp.validator.CapacidadValidatorService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import java.util.List;

@RequiredArgsConstructor
public class BootcampUseCase {

    private final BootcampRepository bootcampRepository;
    private final CapacidadValidatorService capacidadValidatorService;
    private final BootcampEnrichmentService  bootcampEnrichmentService;
    private final BootcampDeletionSagaOrchestrator sagaOrchestrator;
    private final CapacidadGateway capacidadGateway;

    public Mono<Bootcamp> guardarBootcamp(Bootcamp bootcamp) {
        return BootcampValidator.validarCantidadCapacidades(bootcamp.getCapacidadesIds())
                .then(capacidadValidatorService.validarCapacidadesExisten(bootcamp.getCapacidadesIds()))
                .then(calcularTecnologiasIds(bootcamp))
                .flatMap(b -> bootcampRepository.guardarBootcamp(b)
                        .flatMap(bootcampGuardado -> bootcampRepository.guardarRelacionesCapacidades(
                                bootcampGuardado.getId(),
                                bootcampGuardado.getCapacidadesIds()
                        )
                        .then(bootcampRepository.guardarRelacionesTecnologias(
                                bootcampGuardado.getId(),
                                bootcampGuardado.getTecnologiasIds()
                        ))
                        .thenReturn(bootcampGuardado)));
    }

    private Mono<Bootcamp> calcularTecnologiasIds(Bootcamp bootcamp) {
        if (bootcamp.getCapacidadesIds() == null || bootcamp.getCapacidadesIds().isEmpty()) {
            bootcamp.setTecnologiasIds(List.of());
            return Mono.just(bootcamp);
        }

        return capacidadGateway.obtenerCapacidadesPorIds(bootcamp.getCapacidadesIds())
                .map(CapacidadInfo::getTecnologias)
                .collectList()
                .map(listaTecnologias -> {
                    List<Long> tecnologiaIds = listaTecnologias.stream()
                            .flatMap(tecnologias -> tecnologias != null ? tecnologias.stream() : java.util.stream.Stream.empty())
                            .map(tecnologia -> tecnologia.getId())
                            .distinct()
                            .toList();
                    bootcamp.setTecnologiasIds(tecnologiaIds);
                    return bootcamp;
                });
    }

    public Mono<CustomPage<BootcampConCapacidades>> listarBootcamps(
            Integer page,
            Integer size,
            String sortBy,
            String sortDirection
    ) {
        return bootcampRepository.listarBootcamps(page, size, sortBy, sortDirection)
                .flatMap(bootcampEnrichmentService::enriquecerBootcampsConCapacidades);
    }

    public Mono<Void> eliminarBootcamp(String bootcampId) {
        return sagaOrchestrator.deleteBootcampWithSaga(bootcampId);
    }
}