package co.com.bootcamp.model.bootcamp.gateways;

import co.com.bootcamp.model.bootcamp.inscripcion.Inscripcion;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InscripcionRepository {

    Mono<Inscripcion> registrarInscripcion(Inscripcion inscripcion);

    Mono<Long> contarInscripcionesActivasPorPersona(Long personaId);

    Flux<Long> obtenerBootcampIdsDeInscripcionesActivas(Long personaId);

}
