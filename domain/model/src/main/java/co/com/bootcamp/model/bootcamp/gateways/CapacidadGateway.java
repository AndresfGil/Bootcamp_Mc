package co.com.bootcamp.model.bootcamp.gateways;

import reactor.core.publisher.Flux;

import java.util.List;

public interface CapacidadGateway {
    Flux<BootcampInfo> obtenerCapacidadesPorIds(List<Long> ids);
}
