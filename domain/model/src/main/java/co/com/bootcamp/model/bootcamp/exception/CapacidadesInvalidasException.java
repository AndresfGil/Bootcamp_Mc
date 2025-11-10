package co.com.bootcamp.model.bootcamp.exception;

import java.util.List;

public class CapacidadesInvalidasException extends BaseException {

    public CapacidadesInvalidasException(String mensaje) {
        super(
                mensaje,
                "CAPACIDADES_INVALIDAS",
                "Capacidades inválidas",
                400,
                List.of(mensaje)
        );
    }
}

