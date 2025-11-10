package co.com.bootcamp.model.bootcamp.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CapacidadesInvalidasExceptionTest {

    @Test
    void constructor_DeberiaInicializarConMensajeCorrecto() {
        String mensaje = "Un bootcamp debe tener al menos 1 capacidad asociada";
        CapacidadesInvalidasException exception = new CapacidadesInvalidasException(mensaje);

        assertEquals(mensaje, exception.getMessage());
        assertEquals("CAPACIDADES_INVALIDAS", exception.getErrorCode());
        assertEquals("Capacidades inválidas", exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertNotNull(exception.getErrors());
        assertEquals(1, exception.getErrors().size());
        assertEquals(mensaje, exception.getErrors().get(0));
    }

    @Test
    void constructor_ConMensajeDiferente_DeberiaIncluirMensajeEnErrores() {
        String mensaje = "Un bootcamp no puede tener más de 4 capacidades asociadas";
        CapacidadesInvalidasException exception = new CapacidadesInvalidasException(mensaje);

        assertTrue(exception.getErrors().get(0).contains("no puede tener más de"));
    }

    @Test
    void constructor_DeberiaSerInstanciaDeBaseException() {
        CapacidadesInvalidasException exception = new CapacidadesInvalidasException("Test");

        assertTrue(exception instanceof BaseException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void constructor_ConMensajeVacio_DeberiaFuncionarCorrectamente() {
        CapacidadesInvalidasException exception = new CapacidadesInvalidasException("");

        assertEquals("", exception.getMessage());
        assertEquals("", exception.getErrors().get(0));
    }
}

