package co.com.bootcamp.model.bootcamp.exception;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CapacidadNoEncontradaExceptionTest {

    @Test
    void constructor_DeberiaInicializarConValoresCorrectos() {
        List<Long> idsNoEncontrados = Arrays.asList(1L, 2L, 3L);
        CapacidadNoEncontradaException exception = new CapacidadNoEncontradaException(idsNoEncontrados);

        assertEquals("Una o más capacidades no fueron encontradas", exception.getMessage());
        assertEquals("CAPACIDAD_NO_ENCONTRADA", exception.getErrorCode());
        assertEquals("Capacidades no encontradas", exception.getTitle());
        assertEquals(404, exception.getStatusCode());
        assertNotNull(exception.getErrors());
        assertEquals(1, exception.getErrors().size());
        assertTrue(exception.getErrors().get(0).contains("Las siguientes capacidades no existen"));
        assertTrue(exception.getErrors().get(0).contains("[1, 2, 3]"));
    }

    @Test
    void constructor_ConUnSoloId_DeberiaFuncionarCorrectamente() {
        List<Long> idsNoEncontrados = Collections.singletonList(5L);
        CapacidadNoEncontradaException exception = new CapacidadNoEncontradaException(idsNoEncontrados);

        assertEquals(404, exception.getStatusCode());
        assertTrue(exception.getErrors().get(0).contains("[5]"));
    }

    @Test
    void constructor_ConListaVacia_DeberiaFuncionarCorrectamente() {
        List<Long> idsNoEncontrados = Collections.emptyList();
        CapacidadNoEncontradaException exception = new CapacidadNoEncontradaException(idsNoEncontrados);

        assertEquals(404, exception.getStatusCode());
        assertTrue(exception.getErrors().get(0).contains("[]"));
    }

    @Test
    void constructor_DeberiaSerInstanciaDeBaseException() {
        CapacidadNoEncontradaException exception = new CapacidadNoEncontradaException(Arrays.asList(1L));

        assertTrue(exception instanceof BaseException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void constructor_ConMultiplesIds_DeberiaIncluirTodosLosIds() {
        List<Long> idsNoEncontrados = Arrays.asList(10L, 20L, 30L, 40L);
        CapacidadNoEncontradaException exception = new CapacidadNoEncontradaException(idsNoEncontrados);

        String errorMessage = exception.getErrors().get(0);
        assertTrue(errorMessage.contains("10"));
        assertTrue(errorMessage.contains("20"));
        assertTrue(errorMessage.contains("30"));
        assertTrue(errorMessage.contains("40"));
    }

    @Test
    void constructor_DeberiaEstablecerTimestamp() {
        CapacidadNoEncontradaException exception = new CapacidadNoEncontradaException(Arrays.asList(1L));

        assertNotNull(exception.getTimestamp());
    }
}

