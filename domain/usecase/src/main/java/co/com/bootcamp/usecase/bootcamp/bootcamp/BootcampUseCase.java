package co.com.bootcamp.usecase.bootcamp.bootcamp;

import co.com.bootcamp.model.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.BootcampConCapacidades;
import co.com.bootcamp.model.bootcamp.gateways.BootcampRepository;
import co.com.bootcamp.model.bootcamp.page.CustomPage;
import co.com.bootcamp.model.bootcamp.validator.BootcampValidator;
import co.com.bootcamp.usecase.bootcamp.enrichment.BootcampEnrichmentService;
import co.com.bootcamp.usecase.bootcamp.validator.CapacidadValidatorService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BootcampUseCase {

    private final BootcampRepository bootcampRepository;
    private final CapacidadValidatorService capacidadValidatorService;
    private final BootcampEnrichmentService  bootcampEnrichmentService;

    public Mono<Bootcamp> guardarBootcamp(Bootcamp bootcamp) {
        return BootcampValidator.validarCantidadCapacidades(bootcamp.getCapacidadesIds())
                .then(capacidadValidatorService.validarCapacidadesExisten(bootcamp.getCapacidadesIds()))
                .then(Mono.just(bootcamp))
                .flatMap(b -> bootcampRepository.guardarBootcamp(b));
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
}