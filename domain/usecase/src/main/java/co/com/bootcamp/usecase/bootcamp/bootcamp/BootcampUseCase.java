package co.com.bootcamp.usecase.bootcamp.bootcamp;

import co.com.bootcamp.model.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.gateways.BootcampRepository;
import co.com.bootcamp.model.bootcamp.validator.BootcampValidator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BootcampUseCase {

    private final BootcampRepository bootcampRepository;

    public Mono<Bootcamp> guardarBootcamp(Bootcamp bootcamp) {
        return BootcampValidator.validarCantidadCapacidades(bootcamp.getCapacidadesIds())
                .then(Mono.just(bootcamp))
                .flatMap(b -> bootcampRepository.guardarBootcamp(b));
    }
}