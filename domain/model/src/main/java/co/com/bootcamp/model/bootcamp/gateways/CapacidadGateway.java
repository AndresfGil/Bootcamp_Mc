package co.com.bootcamp.model.bootcamp.gateways;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CapacidadGateway {
    Flux<CapacidadInfo> obtenerCapacidadesPorIds(List<Long> ids);
    
    Mono<Void> desactivarCapacidades(List<String> ids);
    
    Mono<Void> activarCapacidades(List<String> ids);
}
