package co.com.bootcamp.usecase.bootcamp.validator;

import co.com.bootcamp.model.bootcamp.exception.CapacidadNoEncontradaException;
import co.com.bootcamp.model.bootcamp.gateways.CapacidadGateway;
import co.com.bootcamp.model.bootcamp.gateways.CapacidadInfo;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class CapacidadValidatorService {

    private final CapacidadGateway capacidadGateway;

    public Mono<Void> validarCapacidadesExisten(List<Long> capacidadesIds) {
        if (capacidadesIds == null || capacidadesIds.isEmpty()) {
            return Mono.empty();
        }

        Set<Long> idsSolicitados = new HashSet<>(capacidadesIds);

        return capacidadGateway.obtenerCapacidadesPorIds(capacidadesIds)
                .map(CapacidadInfo::getId)
                .collectList()
                .flatMap(idsEncontrados -> {
                    Set<Long> idsEncontradosSet = new HashSet<>(idsEncontrados);
                    List<Long> idsNoEncontrados = idsSolicitados.stream()
                            .filter(id -> !idsEncontradosSet.contains(id))
                            .toList();

                    if (!idsNoEncontrados.isEmpty()) {
                        return Mono.error(new CapacidadNoEncontradaException(idsNoEncontrados));
                    }

                    return Mono.empty();
                });
    }
}


