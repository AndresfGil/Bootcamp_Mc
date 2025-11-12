package co.com.bootcamp.model.bootcamp.gateways;

import reactor.core.publisher.Mono;

import java.util.List;

public interface TecnologiaGateway {
    
    Mono<Void> desactivarTecnologias(List<String> ids);
    
    Mono<Void> activarTecnologias(List<String> ids);
}

