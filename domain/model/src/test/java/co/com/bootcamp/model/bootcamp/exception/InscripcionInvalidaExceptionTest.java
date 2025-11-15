package co.com.bootcamp.model.bootcamp.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InscripcionInvalidaExceptionTest {

    @Test
    void constructor_DeberiaInicializarConValoresCorrectos() {
        String mensaje = "No se puede inscribir en más de 5 bootcamps simultáneamente";
        InscripcionInvalidaException exception = new InscripcionInvalidaException(mensaje);

        assertEquals(mensaje, exception.getMessage());
        assertEquals("INSCRIPCION_INVALIDA", exception.getErrorCode());
        assertEquals("Inscripción inválida", exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertNotNull(exception.getErrors());
        assertEquals(1, exception.getErrors().size());
        assertEquals(mensaje, exception.getErrors().get(0));
    }

    @Test
    void constructor_DeberiaSerInstanciaDeBaseException() {
        InscripcionInvalidaException exception = new InscripcionInvalidaException("Error de inscripción");

        assertTrue(exception instanceof BaseException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void constructor_DeberiaEstablecerTimestamp() {
        InscripcionInvalidaException exception = new InscripcionInvalidaException("Error");

        assertNotNull(exception.getTimestamp());
    }

    @Test
    void constructor_ConMensajeLargo_DeberiaFuncionarCorrectamente() {
        String mensajeLargo = "No se puede inscribir en este bootcamp porque se solapa con otro bootcamp activo. El bootcamp inicia el 2024-01-15 y tiene una duración de 12 semanas.";
        InscripcionInvalidaException exception = new InscripcionInvalidaException(mensajeLargo);

        assertEquals(mensajeLargo, exception.getMessage());
        assertEquals(400, exception.getStatusCode());
        assertTrue(exception.getErrors().get(0).contains("solapa"));
    }

    @Test
    void constructor_ConMensajeVacio_DeberiaFuncionarCorrectamente() {
        InscripcionInvalidaException exception = new InscripcionInvalidaException("");

        assertEquals("", exception.getMessage());
        assertEquals(400, exception.getStatusCode());
        assertEquals("", exception.getErrors().get(0));
    }
}

