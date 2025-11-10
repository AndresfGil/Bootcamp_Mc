package co.com.bootcamp.model.bootcamp.validator;

import co.com.bootcamp.model.bootcamp.exception.CapacidadesInvalidasException;
import reactor.core.publisher.Mono;

import java.util.List;

public class BootcampValidator {

    private static final int MIN_CAPACIDADES = 1;
    private static final int MAX_CAPACIDADES = 4;

    public static Mono<Void> validarCantidadCapacidades(List<Long> capacidadesIds) {
        if (capacidadesIds == null || capacidadesIds.isEmpty()) {
            return Mono.error(new CapacidadesInvalidasException(
                    String.format("Un bootcamp debe tener al menos %d capacidad asociada", MIN_CAPACIDADES)
            ));
        }

        if (capacidadesIds.size() > MAX_CAPACIDADES) {
            return Mono.error(new CapacidadesInvalidasException(
                    String.format("Un bootcamp no puede tener más de %d capacidades asociadas", MAX_CAPACIDADES)
            ));
        }

        return Mono.empty();
    }
}

