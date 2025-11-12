package co.com.bootcamp.consumer;

import co.com.bootcamp.consumer.dto.CapacidadBatchRequestDto;
import co.com.bootcamp.consumer.dto.CapacidadBatchResponseDto;
import co.com.bootcamp.model.bootcamp.gateways.CapacidadGateway;
import co.com.bootcamp.model.bootcamp.gateways.CapacidadInfo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class CapacidadRestAdapter implements CapacidadGateway {

    private final WebClient capacidadWebClient;

    public CapacidadRestAdapter(@Qualifier("capacidadWebClient") WebClient capacidadWebClient) {
        this.capacidadWebClient = capacidadWebClient;
    }

    @Override
    @CircuitBreaker(name = "obtenerCapacidadesPorIds")
    public Flux<CapacidadInfo> obtenerCapacidadesPorIds(List<Long> ids) {
        CapacidadBatchRequestDto request = new CapacidadBatchRequestDto(ids);

        return capacidadWebClient.post()
                .uri("/api/capacidad/batch")
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(CapacidadBatchResponseDto.class)
                .map(dto -> CapacidadInfo.builder()
                        .id(dto.id())
                        .nombre(dto.nombre())
                        .tecnologias(dto.tecnologias() != null ? dto.tecnologias() : List.of())
                        .build())
                .onErrorMap(throwable -> new RuntimeException("Error al consultar capacidades: " + throwable.getMessage(), throwable));
    }

    @Override
    @CircuitBreaker(name = "desactivarCapacidades")
    public Mono<Void> desactivarCapacidades(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return Mono.empty();
        }
        
        List<Long> idsLong = ids.stream()
                .map(Long::parseLong)
                .toList();
        
        CapacidadBatchRequestDto request = new CapacidadBatchRequestDto(idsLong);
        
        return capacidadWebClient.patch()
                .uri("/api/capacidad/desactivar")
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .then()
                .onErrorMap(error -> new RuntimeException("Error al desactivar capacidades: " + error.getMessage(), error));
    }

    @Override
    @CircuitBreaker(name = "activarCapacidades")
    public Mono<Void> activarCapacidades(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return Mono.empty();
        }
        
        List<Long> idsLong = ids.stream()
                .map(Long::parseLong)
                .toList();
        
        CapacidadBatchRequestDto request = new CapacidadBatchRequestDto(idsLong);
        
        return capacidadWebClient.patch()
                .uri("/api/capacidad/activar")
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .then()
                .onErrorMap(error -> new RuntimeException("Error al activar capacidades: " + error.getMessage(), error));
    }
}
