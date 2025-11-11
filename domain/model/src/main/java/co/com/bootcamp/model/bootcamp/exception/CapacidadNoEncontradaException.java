package co.com.bootcamp.model.bootcamp.exception;

import java.util.List;

public class CapacidadNoEncontradaException extends BaseException {

    public CapacidadNoEncontradaException(List<Long> idsNoEncontrados) {
        super(
                "Una o más capacidades no fueron encontradas",
                "CAPACIDAD_NO_ENCONTRADA",
                "Capacidades no encontradas",
                404,
                List.of(String.format("Las siguientes capacidades no existen: %s", idsNoEncontrados))
        );
    }
}

