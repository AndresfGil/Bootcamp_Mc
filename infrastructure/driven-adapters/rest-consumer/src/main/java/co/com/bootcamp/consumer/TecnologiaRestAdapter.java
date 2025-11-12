package co.com.bootcamp.consumer;

import co.com.bootcamp.consumer.dto.TecnologiaBatchRequestDto;
import co.com.bootcamp.model.bootcamp.gateways.TecnologiaGateway;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class TecnologiaRestAdapter implements TecnologiaGateway {

    private final WebClient tecnologiaWebClient;

    public TecnologiaRestAdapter(@Qualifier("tecnologiaWebClient") WebClient tecnologiaWebClient) {
        this.tecnologiaWebClient = tecnologiaWebClient;
    }

    @Override
    @CircuitBreaker(name = "desactivarTecnologias")
    public Mono<Void> desactivarTecnologias(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return Mono.empty();
        }
        
        List<Long> idsLong = ids.stream()
                .map(Long::parseLong)
                .toList();
        
        TecnologiaBatchRequestDto request = new TecnologiaBatchRequestDto(idsLong);
        
        return tecnologiaWebClient.patch()
                .uri("/api/tecnologia/desactivar")
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .then()
                .onErrorMap(error -> new RuntimeException("Error al desactivar tecnologías: " + error.getMessage(), error));
    }

    @Override
    @CircuitBreaker(name = "activarTecnologias")
    public Mono<Void> activarTecnologias(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return Mono.empty();
        }
        
        List<Long> idsLong = ids.stream()
                .map(Long::parseLong)
                .toList();
        
        TecnologiaBatchRequestDto request = new TecnologiaBatchRequestDto(idsLong);
        
        return tecnologiaWebClient.patch()
                .uri("/api/tecnologia/activar")
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .then()
                .onErrorMap(error -> new RuntimeException("Error al activar tecnologías: " + error.getMessage(), error));
    }
}

