package co.com.bootcamp.model.bootcamp.inscripcion;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InscripcionTest {

    @Test
    void builder_DeberiaCrearInstanciaConTodosLosCampos() {
        LocalDateTime fecha = LocalDateTime.now();
        Inscripcion inscripcion = Inscripcion.builder()
                .id(1L)
                .personaId(100L)
                .bootcampId(200L)
                .fechaInscripcion(fecha)
                .build();

        assertEquals(1L, inscripcion.getId());
        assertEquals(100L, inscripcion.getPersonaId());
        assertEquals(200L, inscripcion.getBootcampId());
        assertEquals(fecha, inscripcion.getFechaInscripcion());
    }

    @Test
    void builder_DeberiaCrearInstanciaConCamposNulos() {
        Inscripcion inscripcion = Inscripcion.builder().build();

        assertNull(inscripcion.getId());
        assertNull(inscripcion.getPersonaId());
        assertNull(inscripcion.getBootcampId());
        assertNull(inscripcion.getFechaInscripcion());
    }

    @Test
    void noArgsConstructor_DeberiaCrearInstanciaVacia() {
        Inscripcion inscripcion = new Inscripcion();

        assertNull(inscripcion.getId());
        assertNull(inscripcion.getPersonaId());
        assertNull(inscripcion.getBootcampId());
        assertNull(inscripcion.getFechaInscripcion());
    }

    @Test
    void allArgsConstructor_DeberiaCrearInstanciaConTodosLosParametros() {
        LocalDateTime fecha = LocalDateTime.now();
        Inscripcion inscripcion = new Inscripcion(1L, 100L, 200L, fecha);

        assertEquals(1L, inscripcion.getId());
        assertEquals(100L, inscripcion.getPersonaId());
        assertEquals(200L, inscripcion.getBootcampId());
        assertEquals(fecha, inscripcion.getFechaInscripcion());
    }

    @Test
    void toBuilder_DeberiaCrearNuevaInstanciaConValoresModificados() {
        LocalDateTime fechaOriginal = LocalDateTime.now();
        Inscripcion original = Inscripcion.builder()
                .id(1L)
                .personaId(100L)
                .bootcampId(200L)
                .fechaInscripcion(fechaOriginal)
                .build();

        LocalDateTime fechaModificada = LocalDateTime.now().plusDays(1);
        Inscripcion modificada = original.toBuilder()
                .personaId(300L)
                .fechaInscripcion(fechaModificada)
                .build();

        assertEquals(1L, modificada.getId());
        assertEquals(300L, modificada.getPersonaId());
        assertEquals(200L, modificada.getBootcampId());
        assertEquals(fechaModificada, modificada.getFechaInscripcion());
        assertNotSame(original, modificada);
    }

    @Test
    void setters_DeberiaModificarValores() {
        Inscripcion inscripcion = new Inscripcion();
        LocalDateTime fecha = LocalDateTime.now();

        inscripcion.setId(2L);
        inscripcion.setPersonaId(150L);
        inscripcion.setBootcampId(250L);
        inscripcion.setFechaInscripcion(fecha);

        assertEquals(2L, inscripcion.getId());
        assertEquals(150L, inscripcion.getPersonaId());
        assertEquals(250L, inscripcion.getBootcampId());
        assertEquals(fecha, inscripcion.getFechaInscripcion());
    }
}

