package co.com.bootcamp.consumer;

import co.com.bootcamp.consumer.dto.PersonaResponseDto;
import co.com.bootcamp.model.bootcamp.exception.UsuarioNoEncontradoException;
import co.com.bootcamp.model.bootcamp.gateways.PersonaGateway;
import co.com.bootcamp.model.bootcamp.inscripcion.Persona;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class PersonaRestAdapter implements PersonaGateway {

    private final WebClient personaWebClient;

    public PersonaRestAdapter(@Qualifier("personaWebClient") WebClient personaWebClient) {
        this.personaWebClient = personaWebClient;
    }

    @Override
    @CircuitBreaker(name = "obtenerPersonaPorId")
    public Mono<Persona> obtenerPersonaPorId(Long id) {
        return personaWebClient.get()
                .uri("/api/persona/{id}", id)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> 
                    Mono.error(new UsuarioNoEncontradoException("La persona con ID " + id + " no fue encontrada")))
                .onStatus(status -> status.is5xxServerError(), response -> 
                    Mono.error(new RuntimeException("Error del servidor al consultar persona")))
                .bodyToMono(PersonaResponseDto.class)
                .map(dto -> Persona.builder()
                        .id(dto.id())
                        .nombre(dto.nombre())
                        .correo(dto.correo())
                        .build())
                .onErrorMap(throwable -> {
                    if (throwable instanceof UsuarioNoEncontradoException) {
                        return throwable;
                    }
                    return new RuntimeException("Error al consultar persona: " + throwable.getMessage(), throwable);
                });
    }
}
