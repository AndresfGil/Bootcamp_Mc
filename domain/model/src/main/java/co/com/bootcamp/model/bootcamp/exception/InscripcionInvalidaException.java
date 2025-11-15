package co.com.bootcamp.model.bootcamp.exception;

import java.util.List;

public class InscripcionInvalidaException extends BaseException {

    public InscripcionInvalidaException(String mensaje) {
        super(
                mensaje,
                "INSCRIPCION_INVALIDA",
                "Inscripción inválida",
                400,
                List.of(mensaje)
        );
    }
}

