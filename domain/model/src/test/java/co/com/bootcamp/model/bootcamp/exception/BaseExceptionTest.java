package co.com.bootcamp.model.bootcamp.exception;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BaseExceptionTest {

    @Test
    void constructor_DeberiaInicializarTodosLosCampos() {
        String message = "Mensaje de error";
        String errorCode = "ERROR_CODE";
        String title = "Título del error";
        int statusCode = 400;
        List<String> errors = Arrays.asList("Error 1", "Error 2");

        BaseException exception = new BaseException(message, errorCode, title, statusCode, errors) {};

        assertEquals(message, exception.getMessage());
        assertEquals(errorCode, exception.getErrorCode());
        assertEquals(title, exception.getTitle());
        assertEquals(statusCode, exception.getStatusCode());
        assertEquals(errors, exception.getErrors());
        assertNotNull(exception.getTimestamp());
        assertTrue(exception.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void constructor_DeberiaEstablecerTimestampAlMomentoDeCreacion() {
        BaseException exception1 = new BaseException("Error 1", "CODE1", "Title 1", 400, List.of()) {};
        
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        BaseException exception2 = new BaseException("Error 2", "CODE2", "Title 2", 500, List.of()) {};

        assertTrue(exception1.getTimestamp().isBefore(exception2.getTimestamp()) || 
                   exception1.getTimestamp().equals(exception2.getTimestamp()));
    }

    @Test
    void constructor_ConListaVacia_DeberiaFuncionarCorrectamente() {
        BaseException exception = new BaseException("Error", "CODE", "Title", 400, List.of()) {};

        assertNotNull(exception.getErrors());
        assertTrue(exception.getErrors().isEmpty());
    }

    @Test
    void constructor_ConListaNula_DeberiaPermitirNull() {
        BaseException exception = new BaseException("Error", "CODE", "Title", 400, null) {};

        assertNull(exception.getErrors());
    }
}

