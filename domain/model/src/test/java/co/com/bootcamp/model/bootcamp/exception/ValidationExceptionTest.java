package co.com.bootcamp.model.bootcamp.exception;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidationExceptionTest {

    @Test
    void constructor_DeberiaInicializarConValoresCorrectos() {
        List<String> errors = Arrays.asList("Error 1", "Error 2", "Error 3");
        ValidationException exception = new ValidationException(errors);

        assertEquals("Error de validación", exception.getMessage());
        assertEquals("VALIDATION_ERROR", exception.getErrorCode());
        assertEquals("Error de validación", exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertEquals(errors, exception.getErrors());
    }

    @Test
    void constructor_ConListaVacia_DeberiaFuncionarCorrectamente() {
        ValidationException exception = new ValidationException(Collections.emptyList());

        assertNotNull(exception.getErrors());
        assertTrue(exception.getErrors().isEmpty());
    }

    @Test
    void constructor_ConUnSoloError_DeberiaFuncionarCorrectamente() {
        List<String> errors = Collections.singletonList("Campo requerido");
        ValidationException exception = new ValidationException(errors);

        assertEquals(1, exception.getErrors().size());
        assertEquals("Campo requerido", exception.getErrors().get(0));
    }

    @Test
    void constructor_DeberiaSerInstanciaDeBaseException() {
        ValidationException exception = new ValidationException(List.of("Error"));

        assertTrue(exception instanceof BaseException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void constructor_ConListaNula_DeberiaPermitirNull() {
        ValidationException exception = new ValidationException(null);

        assertNull(exception.getErrors());
    }
}

