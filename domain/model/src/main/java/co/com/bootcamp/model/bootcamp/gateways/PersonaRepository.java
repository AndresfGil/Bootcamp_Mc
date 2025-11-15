package co.com.bootcamp.model.bootcamp.gateways;

import co.com.bootcamp.model.bootcamp.inscripcion.Persona;
import reactor.core.publisher.Mono;

public interface PersonaRepository {

    Mono<Persona> consultarPersonaPorId(Long id);

}
