package co.com.bootcamp.model.bootcamp.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BootcampNoEncontradoTest {

    @Test
    void constructor_DeberiaInicializarConValoresCorrectos() {
        String mensaje = "El bootcamp con ID 789 no fue encontrado";
        BootcampNoEncontrado exception = new BootcampNoEncontrado(mensaje);

        assertEquals(mensaje, exception.getMessage());
        assertEquals("BOOTCAMP_ELIMINACION_FALLIDA", exception.getErrorCode());
        assertEquals("Bootcamp no encontrado", exception.getTitle());
        assertEquals(404, exception.getStatusCode());
        assertNotNull(exception.getErrors());
        assertEquals(1, exception.getErrors().size());
        assertEquals(mensaje, exception.getErrors().get(0));
    }

    @Test
    void constructor_DeberiaSerInstanciaDeBaseException() {
        BootcampNoEncontrado exception = new BootcampNoEncontrado("Bootcamp no encontrado");

        assertTrue(exception instanceof BaseException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void constructor_DeberiaEstablecerTimestamp() {
        BootcampNoEncontrado exception = new BootcampNoEncontrado("Error");

        assertNotNull(exception.getTimestamp());
    }

    @Test
    void constructor_ConMensajeConId_DeberiaFuncionarCorrectamente() {
        String mensaje = "El bootcamp con ID 999 no fue encontrado";
        BootcampNoEncontrado exception = new BootcampNoEncontrado(mensaje);

        assertEquals(mensaje, exception.getMessage());
        assertEquals(404, exception.getStatusCode());
        assertTrue(exception.getErrors().get(0).contains("999"));
    }

    @Test
    void constructor_ConMensajeVacio_DeberiaFuncionarCorrectamente() {
        BootcampNoEncontrado exception = new BootcampNoEncontrado("");

        assertEquals("", exception.getMessage());
        assertEquals(404, exception.getStatusCode());
        assertEquals("", exception.getErrors().get(0));
    }
}

