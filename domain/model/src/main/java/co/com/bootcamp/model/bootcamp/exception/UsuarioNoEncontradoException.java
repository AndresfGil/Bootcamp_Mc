package co.com.bootcamp.model.bootcamp.exception;

import java.util.List;

public class UsuarioNoEncontradoException extends BaseException {
    public UsuarioNoEncontradoException(String mensaje) {
        super(
                mensaje,
                "USUARIO_NO_ENCONTRADO",
                "Usuario no encontrado",
                404,
                List.of(mensaje)
        );
    }
}
