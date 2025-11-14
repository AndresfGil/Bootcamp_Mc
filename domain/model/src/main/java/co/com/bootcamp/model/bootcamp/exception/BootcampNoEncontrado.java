package co.com.bootcamp.model.bootcamp.exception;

import java.util.List;

public class BootcampNoEncontrado extends BaseException {

    public BootcampNoEncontrado(String mensaje) {
        super(
                mensaje,
                "BOOTCAMP_ELIMINACION_FALLIDA",
                "Bootcamp no encontrado",
                404,
                List.of(mensaje)
        );
    }
}

