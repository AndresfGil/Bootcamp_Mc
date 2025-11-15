package co.com.bootcamp.model.bootcamp.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioNoEncontradoExceptionTest {

    @Test
    void constructor_DeberiaInicializarConValoresCorrectos() {
        String mensaje = "La persona con ID 123 no fue encontrada";
        UsuarioNoEncontradoException exception = new UsuarioNoEncontradoException(mensaje);

        assertEquals(mensaje, exception.getMessage());
        assertEquals("USUARIO_NO_ENCONTRADO", exception.getErrorCode());
        assertEquals("Usuario no encontrado", exception.getTitle());
        assertEquals(404, exception.getStatusCode());
        assertNotNull(exception.getErrors());
        assertEquals(1, exception.getErrors().size());
        assertEquals(mensaje, exception.getErrors().get(0));
    }

    @Test
    void constructor_DeberiaSerInstanciaDeBaseException() {
        UsuarioNoEncontradoException exception = new UsuarioNoEncontradoException("Usuario no encontrado");

        assertTrue(exception instanceof BaseException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void constructor_DeberiaEstablecerTimestamp() {
        UsuarioNoEncontradoException exception = new UsuarioNoEncontradoException("Error");

        assertNotNull(exception.getTimestamp());
    }

    @Test
    void constructor_ConMensajeConId_DeberiaFuncionarCorrectamente() {
        String mensaje = "La persona con ID 456 no fue encontrada";
        UsuarioNoEncontradoException exception = new UsuarioNoEncontradoException(mensaje);

        assertEquals(mensaje, exception.getMessage());
        assertEquals(404, exception.getStatusCode());
        assertTrue(exception.getErrors().get(0).contains("456"));
    }

    @Test
    void constructor_ConMensajeVacio_DeberiaFuncionarCorrectamente() {
        UsuarioNoEncontradoException exception = new UsuarioNoEncontradoException("");

        assertEquals("", exception.getMessage());
        assertEquals(404, exception.getStatusCode());
        assertEquals("", exception.getErrors().get(0));
    }
}

