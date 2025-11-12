package co.com.bootcamp.model.bootcamp.exception;

import java.util.List;

public class BootcampEliminacionFallidaException extends BaseException {

    public BootcampEliminacionFallidaException(String mensaje) {
        super(
                mensaje,
                "BOOTCAMP_ELIMINACION_FALLIDA",
                "Error al eliminar bootcamp",
                500,
                List.of(mensaje)
        );
    }
}

