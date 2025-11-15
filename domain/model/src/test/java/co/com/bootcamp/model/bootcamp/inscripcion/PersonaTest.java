package co.com.bootcamp.model.bootcamp.inscripcion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonaTest {

    @Test
    void builder_DeberiaCrearInstanciaConTodosLosCampos() {
        Persona persona = Persona.builder()
                .id(1L)
                .nombre("Juan Pérez")
                .correo("juan.perez@example.com")
                .build();

        assertEquals(1L, persona.getId());
        assertEquals("Juan Pérez", persona.getNombre());
        assertEquals("juan.perez@example.com", persona.getCorreo());
    }

    @Test
    void builder_DeberiaCrearInstanciaConCamposNulos() {
        Persona persona = Persona.builder().build();

        assertNull(persona.getId());
        assertNull(persona.getNombre());
        assertNull(persona.getCorreo());
    }

    @Test
    void noArgsConstructor_DeberiaCrearInstanciaVacia() {
        Persona persona = new Persona();

        assertNull(persona.getId());
        assertNull(persona.getNombre());
        assertNull(persona.getCorreo());
    }

    @Test
    void allArgsConstructor_DeberiaCrearInstanciaConTodosLosParametros() {
        Persona persona = new Persona(1L, "María García", "maria.garcia@example.com");

        assertEquals(1L, persona.getId());
        assertEquals("María García", persona.getNombre());
        assertEquals("maria.garcia@example.com", persona.getCorreo());
    }

    @Test
    void toBuilder_DeberiaCrearNuevaInstanciaConValoresModificados() {
        Persona original = Persona.builder()
                .id(1L)
                .nombre("Carlos López")
                .correo("carlos.lopez@example.com")
                .build();

        Persona modificada = original.toBuilder()
                .nombre("Carlos López Actualizado")
                .correo("carlos.actualizado@example.com")
                .build();

        assertEquals(1L, modificada.getId());
        assertEquals("Carlos López Actualizado", modificada.getNombre());
        assertEquals("carlos.actualizado@example.com", modificada.getCorreo());
        assertNotSame(original, modificada);
    }

    @Test
    void setters_DeberiaModificarValores() {
        Persona persona = new Persona();

        persona.setId(2L);
        persona.setNombre("Ana Martínez");
        persona.setCorreo("ana.martinez@example.com");

        assertEquals(2L, persona.getId());
        assertEquals("Ana Martínez", persona.getNombre());
        assertEquals("ana.martinez@example.com", persona.getCorreo());
    }
}

