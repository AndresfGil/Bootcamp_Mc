package co.com.bootcamp.consumer;

import co.com.bootcamp.consumer.dto.CapacidadBatchRequestDto;
import co.com.bootcamp.consumer.dto.CapacidadBatchResponseDto;
import co.com.bootcamp.model.bootcamp.gateways.CapacidadGateway;
import co.com.bootcamp.model.bootcamp.gateways.CapacidadInfo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CapacidadRestAdapter implements CapacidadGateway {

    private final WebClient client;

    @Override
    @CircuitBreaker(name = "obtenerCapacidadesPorIds")
    public Flux<CapacidadInfo> obtenerCapacidadesPorIds(List<Long> ids) {
        CapacidadBatchRequestDto request = new CapacidadBatchRequestDto(ids);

        return client.post()
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
}
